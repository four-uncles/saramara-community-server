package com.kakao.saramaracommunity.comment.controller;

import com.kakao.saramaracommunity.comment.exception.CommentNotFoundException;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 댓글 ExceptionHandler입니다.
 */
@RestControllerAdvice
@Log4j2
public class CommentControllerAdvice {

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCommentNotFoundException(CommentNotFoundException e) {

        return ResponseEntity
                .status(e.getCommentErrorCode().getHttpStatus())
                .body(
                        ApiResponse.errorResponse(
                                e.getCommentErrorCode().getHttpStatus().value(),
                                e.getCommentErrorCode().getMessage()
                        )
                );
    }
}
