
package com.kakao.saramaracommunity.bucket.exception;

import lombok.Getter;

/**
 * BucketUploadOutOfRangeException: AWS S3 버킷에 업로드할 이미지 개수 범위를 벗어났을 경우 발생시킬 예외 클래스
 * 이미지는 1장 ~ 5장까지만 업로드할 수 있습니다.
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 * 해당 예외 메시지를 상위 클래스인 RuntimeException의 메시지로 설정합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class BucketUploadOutOfRangeException extends RuntimeException {

    private final BucketErrorCode statusWithCode;

    public BucketUploadOutOfRangeException(BucketErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
