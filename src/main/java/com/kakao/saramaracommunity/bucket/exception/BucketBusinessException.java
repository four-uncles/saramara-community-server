
package com.kakao.saramaracommunity.bucket.exception;

import lombok.Getter;

/**
 * AWS S3 버킷에 업로드할 때 서버 문제가 발생했을 때의 예외 클래스입니다.
 * Unchecked Exception(Runtimexception)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 */
@Getter
public class BucketBusinessException extends RuntimeException {
    private final BucketErrorCode statusWithCode;
    public BucketBusinessException(BucketErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
