package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.service.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.exception.AttachErrorCode;
import com.kakao.saramaracommunity.attach.exception.AttachNotFoundException;
import com.kakao.saramaracommunity.attach.exception.ImageUploadOutOfRangeException;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
import com.kakao.saramaracommunity.attach.service.dto.request.AttachServiceRequest;
import com.kakao.saramaracommunity.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AttachServiceImpl: 이미지 첨부파일 관련 비즈니스 로직을 수행할 AttachService 서비스 인터페이스의 구현체 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class AttachServiceImpl implements AttachService {

    private final AttachRepository attachRepository;

    private final AwsS3Uploader awsS3Uploader;

    /**
     * uploadS3BucketImage: 1장 이상의 이미지를 S3 버킷에 등록하는 메서드
     * request의 이미지 파일을 AWS S3 버킷에 등록하여 반환받은 객체 URL을 List에 담아 반환
     *
     * @param request List<Multipartfile> imgList
     * @return AttachResponse.UploadBucketResponse
     */
    @Override
    public AttachResponse.UploadBucketResponse uploadS3BucketImages(AttachServiceRequest.UploadBucketRequest request) {

            List<MultipartFile> imgList = request.getImgList();

            if(isImageCntOutOfRange(imgList.size())) {
                throw new ImageUploadOutOfRangeException(AttachErrorCode.ATTACH_IMAGE_RANGE_OUT);
            }

            List<String> result = imgList.stream()
                    .map(img -> {
                        log.info("[AttachServiceImpl] AWS S3 버킷에 업로드할 이미지 정보 - 이미지파일: {}", img);
                        return awsS3Uploader.upload(img);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("AWS S3 버킷에 {}장의 이미지를 정상적으로 등록했습니다.", result.size());
            log.info("S3 버킷 등록 이미지 정보: {}",  result);

            return AttachResponse.UploadBucketResponse.builder()
                    .code(HttpStatus.OK.value())
                    .msg("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.")
                    .data(result)
                    .build();

    }

    /**
     * uploadImages: S3 버킷에 저장된 이미지 객체 URL을 ATTACH 테이블에 저장하는 메서드
     *
     * @param request attachType, ids, imgList
     * @return AttachResponse.UploadResponse
     */
    @Override
    public AttachResponse.UploadResponse uploadImages(AttachServiceRequest.UploadRequest request) {

            AttachType type = request.getAttachType();
            Long id = request.getIds();
            Map<Long, String> imgList = request.getImgList();

            if(isImageCntOutOfRange(imgList.size())) {
                throw new ImageUploadOutOfRangeException(AttachErrorCode.ATTACH_IMAGE_RANGE_OUT);
            }

            List<Attach> attachs = imgList.entrySet()
                    .stream()
                    .map(entry -> {
                        long key = entry.getKey();
                        String imgPath = entry.getValue();
                        log.info("[AttachServiceImpl] 업로드할 이미지 정보 - 이미지순서: {}, 이미지 URL: {}", key, imgPath);
                        return Attach.builder()
                                .type(type)
                                .ids(id)
                                .seq(key)
                                .imgPath(imgPath)
                                .build();
                    })
                    .collect(Collectors.toList());

            log.info("[AttachServiceImpl] DB에 이미지 객체 URL을 저장합니다.");

            attachRepository.saveAll(attachs);

            return AttachResponse.UploadResponse.builder()
                    .code(HttpStatus.OK.value())
                    .msg("정상적으로 DB에 이미지 업로드를 완료했습니다.")
                    .data(true)
                    .build();

    }

    /**
     * getBoardImages: 하나의 게시글에 등록된 이미지 목록(순서: 이미지URL)을 ATTACH 테이블에서 조회하는 메서드
     * 2023.08.01 리턴 타입의 경우 attachId를 포함하여 반환하기 위해 Map<Long, String> 형식에서 Map<Long, Map<Long, String>> 형식으로 변경함.
     *
     * @param request type, id
     * @return AttachResponse.GetImageResponse
     */
    @Override
    public AttachResponse.GetImageResponse getBoardImages(AttachServiceRequest.GetBoardImageRequest request) {

            AttachType type = request.getAttachType();
            Long id = request.getIds();

            List<Attach> attachList = attachRepository.findAllByIds(id);

            if(attachList.isEmpty()) {
                throw new AttachNotFoundException(AttachErrorCode.ATTACH_NOT_FOUND);
            }

            /**
             * 게시글 번호로 조회한 ATTACH 데이터 목록을 파싱 한다.
             * 파싱 형식: {attachId: {seq: imgPath}}
             */
            Map<Long, Map<Long, String>> result = attachList.stream()
                    .collect(Collectors.toMap(
                            Attach::getAttachId,
                            attach -> {
                                Map<Long, String> imgList = new HashMap<>();
                                imgList.put(attach.getSeq(), attach.getImgPath());
                                return imgList;
                            }
                    ));

            log.info("[AttachServiceImpl] 조회할 게시글의 이미지 목록: {}", result);

            return AttachResponse.GetImageResponse.builder()
                    .code(HttpStatus.OK.value())
                    .msg("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다.")
                    .data(result)
                    .build();

    }

    /**
     * getAllBoardImages: 모든 게시글에 등록된 이미지 목록(게시글번호: (순서: 이미지URL))을 ATTACH 테이블에서 조회하는 메서드
     *
     * @return AttachResponse.GetAllImageResponse
     */
    @Override
    public AttachResponse.GetAllImageResponse getAllBoardImages() {

            List<Attach> allAttachList = attachRepository.findAll();

            if(allAttachList.isEmpty()) {
                throw new AttachNotFoundException(AttachErrorCode.ATTACH_NOT_FOUND);
            }

            Map<Long, Map<Long, String>> allboardImageList = allAttachList.stream()
                    .collect(Collectors.groupingBy(Attach::getIds, Collectors.toMap(Attach::getSeq, Attach::getImgPath)));

            log.info("[AttachServiceImpl] getAllBoardImages 모든 게시글의 이미지 목록: {}", allboardImageList);

            return AttachResponse.GetAllImageResponse.builder()
                    .code(HttpStatus.OK.value())
                    .msg("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다.")
                    .data(allboardImageList)
                    .build();

    }

    /**
     * updateImages: 하나의 게시글에 등록된 하나의 이미지를 수정하는 메서드
     *
     * refrectoring
     *
     * @param request attachId, attachType, ids, imgList
     * @return AttachResponse.UpdateResponse
     */
    @Override
    public AttachResponse.UpdateResponse updateImage(AttachServiceRequest.UpdateRequest request) {

            Long attachId = request.getAttachId();
            String imgPath = request.getImgPath();

            Optional<Attach> findAttach = attachRepository.findById(attachId);
            Attach attach = findAttach.orElseThrow(() -> new AttachNotFoundException(AttachErrorCode.ATTACH_NOT_FOUND));

            log.info("[AttachServiceImpl] 이미지 수정 - 기존 이미지 URL: {}", attach.getImgPath());

            attach.changeImgPath(imgPath);
            attachRepository.save(attach);

            log.info("[AttachServiceImpl] 이미지 수정 - 수정 이미지 URL: {}", imgPath);

            return AttachResponse.UpdateResponse.builder()
                    .code(HttpStatus.OK.value())
                    .msg("정상적으로 게시글의 이미지를 수정했습니다.")
                    .data(true)
                    .build();

    }

    /**
     * deleteImage: 하나의 게시글에 등록된 하나의 이미지를 삭제하는 메서드
     * 요청 성공시 HttpStatus 204를 반환합니다.
     *
     * @param attachId
     * @return AttachResponse.DeleteResponse
     */
    @Override
    public AttachResponse.DeleteResponse deleteImage(Long attachId) {

            Optional<Attach> attach = attachRepository.findById(attachId);

            if(attach.isEmpty()) {
                log.error("[AttachServiceImpl] 존재하지 않는 이미지 URL 이기에 삭제할 수 없습니다.");
                throw new AttachNotFoundException(AttachErrorCode.ATTACH_NOT_FOUND);
            }

            log.info("[AttachServiceImpl] 삭제할 이미지 첨부파일 번호(PK): {}", attachId);

            attachRepository.deleteById(attachId);

            return AttachResponse.DeleteResponse.builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .msg("정상적으로 이미지를 삭제했습니다.")
                    .data(true)
                    .build();

    }

    /**
     * checkImageCnt: 이미지 업로드 및 저장시 이미지 개수를 확인할 메서드
     * 시스템 정책상 1장 ~ 5장까지만 업로드가 가능합니다.
     * 0장이나, 6장 이상의 이미지 업로드 요청이 올 경우 ATTACH_IMAGE_RANGE_OUT 예외를 발생시킵니다.
     *
     * @param size
     */
    private boolean isImageCntOutOfRange(int size) {
        return size < 1 || size > 5;
    }
}
