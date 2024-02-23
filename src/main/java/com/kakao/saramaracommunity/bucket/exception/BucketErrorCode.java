package com.kakao.saramaracommunity.bucket.exception;

import com.kakao.saramaracommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Bucket과 관련된 에러코드를 담은 Enum입니다.
 */
@Getter
@RequiredArgsConstructor
public enum BucketErrorCode implements ErrorCode {

    BUCKET_IMAGE_MIN_RANGE_OUT(BAD_REQUEST, "이미지는 최소 1장부터 업로드할 수 있습니다."),
    BUCKET_IMAGE_MAX_RANGE_OUT(BAD_REQUEST, "이미지는 최대 5장까지 업로드할 수 있습니다."),
    BUCKET_IMAGE_FILE_NOT_FOUND(BAD_REQUEST, "업로드할 이미지 파일이 존재하지 않습니다."),
    BUCKET_AWS_S3_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "AWS S3 버킷으로 이미지를 업로드하던 중 문제가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
