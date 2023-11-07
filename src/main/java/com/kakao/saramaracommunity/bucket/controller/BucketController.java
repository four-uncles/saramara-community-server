package com.kakao.saramaracommunity.bucket.controller;

import com.kakao.saramaracommunity.bucket.service.BucketService;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketCreateResponse;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 클라이언트의 요청을 받아 AWS S3 버킷에 이미지 업로드를 수행할 컨트롤러 클래스입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bucket")
public class BucketController {

    private final BucketService bucketService;

    @PostMapping
    public ResponseEntity<ApiResponse<BucketCreateResponse>> uploadImages(
            @RequestPart List<MultipartFile> request
    ) {
        BucketCreateResponse response = bucketService.uploadImages(request);
        return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK,
                        "정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.",
                        response
                )
        );
    }

}
