package com.kakao.saramaracommunity.bucket.controller;

import com.kakao.saramaracommunity.bucket.exception.BucketUploadException;
import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BucketControllerAdvice {

    /**
     * 업로드한 이미지의 개수가 정한 범위를 벗어났을 경우 발생하는 예외를 핸들링하는 메서드입니다.
     * 이미지 개수 범위는 0.0.1 버전 기준 1장부터 5장까지입니다.
     *
     * @param exception 이미지 개수 범위를 벗어났을 경우
     * @return Status 코드와 예외 메시지 반환
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BucketUploadOutOfRangeException.class)
    public ResponseEntity<ApiResponse> handleImageUploadOutOfRange(BucketUploadOutOfRangeException exception) {

        log.error("[ImageUploadOutOfRangeException] AWS S3 버킷에 이미지를 업로드하기 위한 이미지 개수가 0장 혹은 6장 이상입니다.");

        return ResponseEntity
                .status(exception.getStatusWithCode().getHttpStatus())
                .body(
                        ApiResponse.errorResponse(
                                exception.getStatusWithCode().getHttpStatus().value(),
                                exception.getStatusWithCode().getMessage()
                        )
                );
    }

    /**
     * AWS S3 버킷에 이미지를 업로드하던중 서버 문제 발생시 핸들링할 예외 메서드입니다.
     *
     * @param exception AWS S3 버킷 등록중 서버 문제 발생시
     * @return Status 코드와 예외 메시지 반환
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BucketUploadException.class)
    public ResponseEntity<ApiResponse> handleBucketUploadFailed(BucketUploadException exception) {

        log.error("[BucketUploadException] AWS S3 버킷에 이미지를 업로드하던 중 서버 문제가 발생했습니다.");

        return ResponseEntity
                .status(exception.getStatusWithCode().getHttpStatus())
                .body(
                        ApiResponse.errorResponse(
                                exception.getStatusWithCode().getHttpStatus().value(),
                                exception.getStatusWithCode().getMessage()
                        )
                );
    }

}
