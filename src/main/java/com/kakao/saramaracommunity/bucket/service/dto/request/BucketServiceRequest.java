package com.kakao.saramaracommunity.bucket.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Business Layer에서 Bucket(AWS S3 버킷에 등록할 이미지) 관련 요청 DTO를 관리하는 클래스입니다.
 */
@Getter
public class BucketServiceRequest {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BucketUploadRequest {

        private List<MultipartFile> imgList;

        @Builder
        private BucketUploadRequest(List<MultipartFile> imgList) {
            this.imgList = imgList;
        }

    }

}
