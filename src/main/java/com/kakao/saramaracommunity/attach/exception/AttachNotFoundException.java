package com.kakao.saramaracommunity.attach.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AttachNotFoundException: Attach(첨부 이미지)를 찾지 못할 경우 발생시킬 예외 클래스
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachNotFoundException extends RuntimeException {

    private final AttachErrorCode statusWithCode;

    public AttachNotFoundException(AttachErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
