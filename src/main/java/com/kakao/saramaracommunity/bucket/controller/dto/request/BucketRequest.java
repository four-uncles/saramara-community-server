package com.kakao.saramaracommunity.bucket.controller.dto.request;

import com.kakao.saramaracommunity.bucket.service.dto.request.BucketServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * BucketRequest: Presentation Layer에서 Bucket(AWS S3 버킷에 등록할 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class BucketRequest {

    /**
     * UploadBucketRequest: 게시글의 이미지 목록을 AWS S3에 등록하기 위한 요청 DTO 클래스
     * imgList: 이미지 파일(MultipartFile)이 담긴 ArrayList
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BucketUploadRequest {

        @NotNull(message = "이미지 목록이 비어있습니다.")
        private List<MultipartFile> imgList;

        @Builder
        private BucketUploadRequest(List<MultipartFile> imgList) {
            this.imgList = imgList;
        }

        public BucketServiceRequest.BucketUploadRequest toServiceRequest() {
            return BucketServiceRequest.BucketUploadRequest.builder()
                    .imgList(imgList)
                    .build();
        }

    }

}
