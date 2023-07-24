package com.kakao.saramaracommunity.comment.exception;

import com.kakao.saramaracommunity.common.exception.CommonErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode implements CommonErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "댓글을 찾을 수 없습니다.");

    private final Integer httpStatus;
    private final String message;

}