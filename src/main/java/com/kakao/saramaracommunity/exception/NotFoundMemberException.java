package com.kakao.saramaracommunity.exception;

import lombok.Builder;

@Builder
public class NotFoundMemberException extends RuntimeException {
    public String message;
    public Throwable cause;
    public NotFoundMemberException() {
        super();
    }
    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundMemberException(String message) {
        super(message);
    }
    public NotFoundMemberException(Throwable cause) {
        super(cause);
    }
}
