package com.kakao.saramaracommunity.bucket.service.dto.response;

import com.kakao.saramaracommunity.bucket.controller.response.BucketUploadResponse;
import com.kakao.saramaracommunity.common.dto.ConvertDtoByPresentationLayer;

import java.util.List;

public record BucketUploadServiceResponse(
        List<String> images
) implements ConvertDtoByPresentationLayer<BucketUploadResponse> {

    public static BucketUploadServiceResponse of(List<String> images) {
        return new BucketUploadServiceResponse(images);
    }

    @Override
    public BucketUploadResponse toApiResponse() {
        return BucketUploadResponse.of(images);
    }

}
