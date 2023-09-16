package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.bucket.service.dto.request.BucketServiceRequest;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketResponse;
import com.kakao.saramaracommunity.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_RANGE_OUT;


/**
 * BucketServiceImpl: AWS S3 버킷에 클라이언트의 요청 이미지를 업로드하기 위한 비즈니스 로직을 수행할 서비스 인터페이스의 구현체 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final AwsS3Uploader awsS3Uploader;

    /**
     * bucketUploadImages: 1장 이상의 이미지를 S3 버킷에 등록하는 메서드
     * request의 이미지 파일을 AWS S3 버킷에 등록하여 반환받은 객체 URL을 List에 담아 반환
     * 0장이거나 6장 이상의 이미지를 요청으로 받을 경우, ATTACH_IMAGE_RANGE_OUT 예외 발생
     *
     * @param request List<Multipartfile> imgList
     * @return BucketResponse.BucketUploadResponse
     */
    @Override
    public BucketResponse.BucketUploadResponse bucketUploadImages(BucketServiceRequest.BucketUploadRequest request) {

        List<MultipartFile> imgList = request.getImgList();

        if(isImageCntOutOfRange(imgList.size())) {
            throw new BucketUploadOutOfRangeException(BUCKET_IMAGE_RANGE_OUT);
        }

        List<String> result = imgList.stream()
                .map(img -> {
                    log.info("[BucketServiceImpl] AWS S3 버킷에 업로드할 이미지 정보 - 이미지파일: {}", img);
                    return awsS3Uploader.upload(img);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("[BucketServiceImpl] AWS S3 버킷에 {}장의 이미지를 정상적으로 등록했습니다.", result.size());
        log.info("[BucketServiceImpl] S3 버킷 등록 이미지 정보: {}", result);

        return BucketResponse.BucketUploadResponse.of(
                HttpStatus.OK.value(),
                "정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.",
                result
        );
    }

    /**
     * isImageCntOutOfRange: 이미지 업로드 및 저장시 이미지 개수를 확인할 메서드
     * 시스템 정책상 1장 ~ 5장까지만 업로드가 가능합니다.
     * 0장 혹은 6장 이상의 이미지 업로드 요청이 올 경우 ATTACH_IMAGE_RANGE_OUT 예외를 발생시킵니다.
     *
     * @param imgCnt
     */
    private boolean isImageCntOutOfRange(int imgCnt) {
        return imgCnt < 1 || imgCnt > 5;
    }

}
