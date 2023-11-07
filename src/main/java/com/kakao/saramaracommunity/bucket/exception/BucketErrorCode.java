package com.kakao.saramaracommunity.bucket.exception;

import com.kakao.saramaracommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Bucket과 관련된 에러코드를 담은 Enum입니다.
 */
@Getter
@RequiredArgsConstructor
public enum BucketErrorCode implements ErrorCode {

    BUCKET_IMAGE_RANGE_OUT(HttpStatus.BAD_REQUEST, "이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다."),

    BUCKET_AWS_S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 버킷으로 이미지를 업로드하던 중 문제가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

}
