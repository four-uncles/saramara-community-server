
package com.kakao.saramaracommunity.bucket.exception;

import lombok.Getter;

/**
 * AWS S3 버킷에 업로드할 이미지 개수 범위를 벗어났을 경우 발생시킬 예외 클래스입니다.
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 */
@Getter
public class BucketUploadOutOfRangeException extends RuntimeException {

    private final BucketErrorCode statusWithCode;

    public BucketUploadOutOfRangeException(BucketErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
