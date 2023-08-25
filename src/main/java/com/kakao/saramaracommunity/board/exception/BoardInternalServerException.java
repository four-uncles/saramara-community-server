package com.kakao.saramaracommunity.board.exception;

import lombok.Getter;


/**
 * BoardInternalServerException: Board(게시글)의 작성, 조회, 수정, 삭제에 서버 문제가 발생할 경우 발생시킬 예외 클래스
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행.
 */
@Getter
public class BoardInternalServerException extends RuntimeException {

    private final BoardErrorCode statusWithCode;

    public BoardInternalServerException(BoardErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
