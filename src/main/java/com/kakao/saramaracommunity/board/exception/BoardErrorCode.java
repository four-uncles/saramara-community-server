package com.kakao.saramaracommunity.board.exception;

import org.springframework.http.HttpStatus;

import com.kakao.saramaracommunity.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Getter
public enum BoardErrorCode implements ErrorCode {

    BOARD_IMAGE_RANGE_OUT(BAD_REQUEST, "게시글의 이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다."),
    BOARD_VOTE_IMAGE_RANGE_OUT(BAD_REQUEST, "투표(Vote) 카테고리의 게시글은 이미지가 반드시 2장 이상, 5장 이하이어야 합니다."),
    BOARD_CHOICE_IMAGE_RANGE_OUT(BAD_REQUEST, "찬반(Choice) 카테고리의 게시글은 이미지가 반드시 1장이어야 합니다."),
    BOARD_CATEGORY_MISMATCH(BAD_REQUEST, "게시글의 카테고리는 변경할 수 없습니다."),
    UNAUTHORIZED_TO_UPDATE_BOARD(FORBIDDEN, "권한이 없는 사용자입니다."),
    BOARD_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
