package com.kakao.saramaracommunity.bucket.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BucketCreateResponse {

    private final List<String> images;

    @Builder
    private BucketCreateResponse(final List<String> images) {
        this.images = images;
    }

    public static BucketCreateResponse of(List<String> images) {
        return BucketCreateResponse.builder()
                .images(images)
                .build();
    }
}
