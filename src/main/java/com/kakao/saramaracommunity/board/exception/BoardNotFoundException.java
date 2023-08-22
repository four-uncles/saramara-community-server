package com.kakao.saramaracommunity.board.exception;

import lombok.Getter;

/**
 * BoardNotFoundException: Board(게시글)를 찾지 못할 경우 발생시킬 예외 클래스
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행.
 */
@Getter
public class BoardNotFoundException extends RuntimeException {

    private final BoardErrorCode statusWithCode;

    public BoardNotFoundException(BoardErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
