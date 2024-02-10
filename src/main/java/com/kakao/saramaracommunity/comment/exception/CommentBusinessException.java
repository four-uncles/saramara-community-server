package com.kakao.saramaracommunity.comment.exception;

import lombok.Getter;

@Getter
public class CommentBusinessException extends RuntimeException{

    private final CommentErrorCode statusWithCode;

    public CommentBusinessException(CommentErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }
}
