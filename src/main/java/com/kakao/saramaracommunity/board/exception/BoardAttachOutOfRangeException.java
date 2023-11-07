package com.kakao.saramaracommunity.board.exception;

import lombok.Getter;

/**
 * Board(게시글)의 첨부파일 개수 범위를 벗어났을 경우 발생시킬 예외 클래스입니다.
 * Unchecked Exception(RuntimeException)을 상속받기에 예외가 발생하면 롤백을 수행합니다.
 */
@Getter
public class BoardAttachOutOfRangeException extends RuntimeException {

    private final BoardErrorCode statusWithCode;

    public BoardAttachOutOfRangeException(BoardErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
