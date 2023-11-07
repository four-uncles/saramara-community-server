package com.kakao.saramaracommunity.bucket.controller.dto.request;

import com.kakao.saramaracommunity.bucket.service.dto.request.BucketServiceCreateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class BucketCreateRequest {

    @NotNull(message = "이미지 목록은 필수입니다.")
    private final List<MultipartFile> imgList;

    @Builder
    private BucketCreateRequest(final List<MultipartFile> imgList) {
        this.imgList = imgList;
    }
    public BucketServiceCreateRequest toServiceRequest() {
        return BucketServiceCreateRequest.builder()
                .imgList(imgList)
                .build();
    }

}
