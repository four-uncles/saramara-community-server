package com.kakao.saramaracommunity.bucket.dto.business;

import lombok.Builder;

import java.util.List;

@Builder
public record BucketCreateResponse(
        List<String> images
) {
    public static BucketCreateResponse of(List<String> images) {
        return BucketCreateResponse.builder()
                .images(images)
                .build();
    }
}
