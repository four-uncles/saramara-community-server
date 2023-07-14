package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
import com.kakao.saramaracommunity.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AttachServiceImpl: 이미지 첨부파일 관련 비즈니스 로직을 수행할 AttachService 서비스 인터페이스의 구현체 클래스
 *
 * 비즈니스 로직 내부에서 전역 예외 처리에 대한 부분과 로그 출력이 미흡한 상태입니다.
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
     * uploadMultipleImage: 1장 이상의 이미지를 S3 버킷에 업로드 한후, ATTACH 테이블에 저장하는 메서드
     * request의 이미지 순서와 파일이 담긴 Map을 파싱하여 List에 담은 후 ATTACH 테이블에 삽입
     *
     * @param request type, id, imgList
     * @return AttachResponse.UploadResponse
     */
    @Override
    public AttachResponse.UploadResponse uploadImage(AttachRequest.UploadRequest request) {

        try {

            AttachType type = request.getAttachType();
            Long id = request.getIds();
            Map<Long, MultipartFile> imgList = request.getImgList();
            List<Attach> attachs = new ArrayList<>();

            if(imgList.size() < 1 || imgList.size() > 5) {
                log.error("이미지는 1장 이상, 최대 5장까지 등록할 수 있습니다.");
                return AttachResponse.UploadResponse.builder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST))
                        .msg("이미지는 1장 이상, 최대 5장까지 등록할 수 있습니다.")
                        .data(false)
                        .build();
            }

            for (long key : imgList.keySet()) {
                log.info("[AttachServiceImpl] 업로드할 이미지 정보 - 이미지순서: {}, 이미지파일: {}", key, imgList.get(key));
                String imgPath = awsS3Uploader.upload(imgList.get(key));
                if (imgPath != null) {
                    Attach attach = Attach.builder()
                            .type(type)
                            .ids(id)
                            .seq(key)
                            .imgPath(imgPath)
                            .build();
                    attachs.add(attach);
                }
            }

            log.info("[AttachServiceImpl] S3 버킷 업로드 완료 후, DB에 URL을 저장합니다.");
            attachRepository.saveAll(attachs);

            return AttachResponse.UploadResponse.builder()
                    .code(String.valueOf(HttpStatus.OK))
                    .msg("정상적으로 이미지 업로드를 완료했습니다.")
                    .data(true)
                    .build();

        } catch (Exception e) {

            log.error("error: ", e);
            return AttachResponse.UploadResponse.builder()
                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .msg("이미지를 업로드하던 중 문제가 발생했습니다.")
                    .data(false)
                    .build();

        }

    }

    /**
     * getBoardImages: 하나의 게시글에 등록된 이미지 목록(순서: 이미지URL)을 ATTACH 테이블에서 조회하는 메서드
     *
     * @param request type, id
     * @return AttachResponse.GetImageResponse
     */
    @Override
    public AttachResponse.GetImageResponse getBoardImages(AttachRequest.GetBoardImageRequest request) {

        try {

            AttachType type = request.getAttachType();
            Long id = request.getIds();

            List<Attach> attachList = attachRepository.findAllByIds(id);

            Map<Long, String> boardImageList = attachList.stream()
                    .collect(Collectors.toMap(Attach::getSeq, Attach::getImgPath));

            log.info("[AttachServiceImpl] 조회할 게시글의 이미지 목록: {}", boardImageList);

            return AttachResponse.GetImageResponse.builder()
                    .code(String.valueOf(HttpStatus.OK))
                    .msg("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다.")
                    .data(boardImageList)
                    .build();

        } catch (Exception e) {

            log.error("error: ", e);
            return AttachResponse.GetImageResponse.builder()
                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .msg("이미지를 가져오는 중 문제가 발생했습니다.")
                    .data(null)
                    .build();

        }

    }

    /**
     * getAllBoardImages: 모든 게시글에 등록된 이미지 목록(게시글번호: (순서: 이미지URL))을 ATTACH 테이블에서 조회하는 메서드
     *
     * @return AttachResponse.GetAllImageResponse
     */
    @Override
    public AttachResponse.GetAllImageResponse getAllBoardImages() {

        try {

            List<Attach> allAttachList = attachRepository.findAll();

            Map<Long, Map<Long, String>> allboardImageList = allAttachList.stream()
                    .collect(Collectors.groupingBy(Attach::getIds, Collectors.toMap(Attach::getSeq, Attach::getImgPath)));

            log.info("[AttachServiceImpl] getAllBoardImages 모든 게시글의 이미지 목록: {}", allboardImageList);

            return AttachResponse.GetAllImageResponse.builder()
                    .code(String.valueOf(HttpStatus.OK))
                    .msg("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다.")
                    .data(allboardImageList)
                    .build();

        } catch (Exception e) {

            log.error("error: ", e);
            return AttachResponse.GetAllImageResponse.builder()
                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .msg("모든 게시글의 이미지를 가져오는 중 문제가 발생했습니다.")
                    .data(null)
                    .build();

        }
    }

}
