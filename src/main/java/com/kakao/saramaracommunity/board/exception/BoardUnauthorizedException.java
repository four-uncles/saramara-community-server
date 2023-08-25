package com.kakao.saramaracommunity.board.exception;

import lombok.Getter;

/**
 * BoardUnauthorizedException: 사용자의 권한이 없을 때 발생시키는 예외 클래스
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행.
 */
@Getter
public class BoardUnauthorizedException extends RuntimeException{

    private final BoardErrorCode statusWithCode;

    public BoardUnauthorizedException(BoardErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
