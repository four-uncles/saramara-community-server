package com.kakao.saramaracommunity.board.exception;

import org.springframework.http.HttpStatus;

import com.kakao.saramaracommunity.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardErrorCode implements ErrorCode {

    /**
     * 500 ERROR
     * BOARD_CREATE_FAILED: 게시글 작성에 서버 문제가 발생할 경우 500 에러코드를 반환
     * BOARD_UPDATE_FAILED: 게시글 수정에 서버 문제가 발생할 경우 500 에러코드를 반환
     * BOARD_DELETE_FAILED: 게시글 삭제에 서버 문제가 발생할 경우 500 에러코드를 반환
     */
    BOARD_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 작성에 실패하였습니다."),
    BOARD_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정에 실패하였습니다."),
    BOARD_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패하였습니다."),


    /**
     * 403 ERROR
     * UNAUTHORIZED_TO_UPDATE_BOARD: 게시글 작성, 수정, 삭제에 연관되어 있는 회원 고유 PK가 DB에 없는 경우 사용자가 없다 판단하여, 403 에러코드를 반환
     */
    UNAUTHORIZED_TO_UPDATE_BOARD(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    /**
     * 404 ERROR
     * BOARD_NOT_FOUND: 게시글 조회, 수정 및 삭제에서 대상 게시글이 DB에 없는 경우, 404 에러코드를 반환
     */
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;
}
