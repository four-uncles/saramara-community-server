package com.kakao.saramaracommunity.board.exception;

import lombok.Getter;

/**
 * Board(게시글)의 예외 클래스입니다.
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 */
@Getter
public class BoardBusinessException extends RuntimeException {
    private final BoardErrorCode statusWithCode;

    public BoardBusinessException(BoardErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
