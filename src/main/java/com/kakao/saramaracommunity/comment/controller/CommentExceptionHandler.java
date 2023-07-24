package com.kakao.saramaracommunity.comment.controller;

import com.kakao.saramaracommunity.comment.exception.CommentErrorCode;
import com.kakao.saramaracommunity.comment.exception.CommentNotFoundException;
import com.kakao.saramaracommunity.common.dto.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 댓글 ExceptionHandler입니다.
 */
@RestControllerAdvice
@Log4j2
public class CommentExceptionHandler {

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Payload> handleCommentNotFoundException(CommentNotFoundException e) {

        Payload payload = Payload.errorPayload(
                e.getCommentErrorCode().getHttpStatus(),
                e.getCommentErrorCode().getMessage()
        );

        return ResponseEntity.status(e.getCommentErrorCode().getHttpStatus()).body(payload);
    }
}
