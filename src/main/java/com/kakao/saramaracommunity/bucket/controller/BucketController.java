package com.kakao.saramaracommunity.bucket.controller;

import com.kakao.saramaracommunity.bucket.controller.dto.request.BucketRequest;
import com.kakao.saramaracommunity.bucket.service.BucketService;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BucketController: 클라이언트의 요청을 받아 AWS S3 버킷에 이미지 업로드를 수행할 컨트롤러 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bucket")
public class BucketController {

    private final BucketService bucketService;

    /**
     * bucketUploadImages: AWS S3 Bucket 이미지 파일 등록 API
     * URL: POST /api/v1/bucket
     *
     * @param request type, id, imgList
     * @return BucketResponse.BucketUploadResponse
     */
    @PostMapping("/upload/bucket")
    public ResponseEntity<BucketResponse.BucketUploadResponse> bucketUploadImages(@RequestBody @Valid BucketRequest.BucketUploadRequest request) {
        BucketResponse.BucketUploadResponse response = bucketService.bucketUploadImages(request.toServiceRequest());
        return ResponseEntity.ok().body(response);
    }

}
