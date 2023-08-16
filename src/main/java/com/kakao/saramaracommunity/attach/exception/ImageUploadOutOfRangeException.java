package com.kakao.saramaracommunity.attach.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * ImageUploadOutOfRangeException: Attach(첨부 이미지)의 업로드 개수 범위가 아닐 경우 발생시킬 예외 클래스
 * 이미지는 1장 ~ 5장까지만 업로드할 수 있습니다.
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 * 해당 예외 메시지를 상위 클래스인 RuntimeException의 메시지로 설정합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class ImageUploadOutOfRangeException extends RuntimeException {

    private final AttachErrorCode statusWithCode;

    public ImageUploadOutOfRangeException(AttachErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
