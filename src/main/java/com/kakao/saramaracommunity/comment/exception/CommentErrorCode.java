package com.kakao.saramaracommunity.comment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.kakao.saramaracommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode implements ErrorCode {

    COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;

    private final String message;

}