package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketCreateResponse;
import com.kakao.saramaracommunity.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_RANGE_OUT;


/**
 * AWS S3 버킷에 클라이언트의 요청 이미지를 업로드하기 위한 비즈니스 로직을 수행할 서비스 인터페이스의 구현체 클래스입니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final AwsS3Uploader awsS3Uploader;

    @Override
    public BucketCreateResponse uploadImages(List<MultipartFile> imageFiles) {
        if(isImageCntOutOfRange(imageFiles.size())) {
            throw new BucketUploadOutOfRangeException(BUCKET_IMAGE_RANGE_OUT);
        }

        List<String> result = uploadWithGetUrls(imageFiles);

        log.info("[BucketServiceImpl] AWS S3 버킷에 {}장의 이미지를 정상적으로 등록했습니다.", result.size());
        log.info("[BucketServiceImpl] S3 버킷 등록 이미지 정보: {}", result);

        return BucketCreateResponse.of(result);
    }

    private boolean isImageCntOutOfRange(int imgCnt) {
        return imgCnt < 1 || imgCnt > 5;
    }

    private List<String> uploadWithGetUrls(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(img -> {
                    log.info("[BucketServiceImpl] AWS S3 버킷에 업로드할 이미지 정보 - 이미지파일: {}", img);
                    return awsS3Uploader.upload(img);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
