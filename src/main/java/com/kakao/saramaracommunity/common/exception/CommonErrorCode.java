package com.kakao.saramaracommunity.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * AttachRequest: Attach(첨부 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {

    /**
     * 전역으로 내려줄 HttpStatus Code와 메시지입니다.
     * 400, 401, 403, 404, 405, 500
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자의 요청입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한이 없는 사용자의 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드가 호출되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버에서 문제가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

}
