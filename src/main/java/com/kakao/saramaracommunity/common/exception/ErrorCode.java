package com.kakao.saramaracommunity.common.exception;


import org.springframework.http.HttpStatus;

/**
 * 다른 패키지에서도 사용할 수 있게 인터페이스타입으로 작성한 ErrorCode입니다.
 */
public interface ErrorCode {

    String name();

    HttpStatus getHttpStatus();

    String getMessage();

}
