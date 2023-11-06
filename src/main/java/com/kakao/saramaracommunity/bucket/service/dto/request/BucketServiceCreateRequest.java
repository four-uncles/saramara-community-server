package com.kakao.saramaracommunity.bucket.service.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class BucketServiceCreateRequest {

    private final List<MultipartFile> imgList;

    @Builder
    private BucketServiceCreateRequest(List<MultipartFile> imgList) {
        this.imgList = imgList;
    }
}
