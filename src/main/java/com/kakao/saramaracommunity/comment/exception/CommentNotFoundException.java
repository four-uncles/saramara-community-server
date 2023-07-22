package com.kakao.saramaracommunity.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentNotFoundException extends RuntimeException {

    private final CommentErrorCode commentErrorCode;
}
