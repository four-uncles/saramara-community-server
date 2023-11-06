package com.kakao.saramaracommunity.board.exception;

import org.springframework.http.HttpStatus;

import com.kakao.saramaracommunity.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardErrorCode implements ErrorCode {

    ATTACH_BOARD_RANGE_OUT(HttpStatus.BAD_REQUEST, "게시글의 이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다."),
    UNAUTHORIZED_TO_UPDATE_BOARD(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
