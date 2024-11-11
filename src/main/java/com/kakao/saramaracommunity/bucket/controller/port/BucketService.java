package com.kakao.saramaracommunity.bucket.controller.port;

import com.kakao.saramaracommunity.bucket.controller.response.BucketUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * AWS S3 버킷 관련 비즈니스 로직을 수행할 서비스 인터페이스입니다.
 */
public interface BucketService {
    BucketUploadResponse uploadImages(List<MultipartFile> request);
}
