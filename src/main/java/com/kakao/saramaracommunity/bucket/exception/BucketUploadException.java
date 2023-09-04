
package com.kakao.saramaracommunity.bucket.exception;

import lombok.Getter;

/**
 * BucketUploadException: AWS S3 버킷에 업로드할 때 서버 문제가 발생했을 때의 예외 클래스
 * Unchecked Exception(Runtimexception)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 * 해당 예외 메시지를 상위 클래스인 RuntimeException의 메시지로 설정합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class BucketUploadException extends RuntimeException {

    private final BucketErrorCode statusWithCode;

    public BucketUploadException(BucketErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
