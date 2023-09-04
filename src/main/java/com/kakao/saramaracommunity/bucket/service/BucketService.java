package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.service.dto.request.BucketServiceRequest;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketResponse;

public interface BucketService {
    BucketResponse.BucketUploadResponse bucketUploadImages(BucketServiceRequest.BucketUploadRequest request);
}
