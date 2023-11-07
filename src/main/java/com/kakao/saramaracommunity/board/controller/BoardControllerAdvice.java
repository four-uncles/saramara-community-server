package com.kakao.saramaracommunity.board.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kakao.saramaracommunity.board.exception.BoardNotFoundException;
import com.kakao.saramaracommunity.common.response.ApiResponse;

import lombok.extern.log4j.Log4j2;

/**
 * BoardControllerAdvice: Board(게시글) 관련 예외를 처리할 클래스
 * 가장 높은 우선순위로 예외 핸들링
 */
@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BoardControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiResponse> handleBoardNotFound(BoardNotFoundException exception) {
        return ResponseEntity
            .status(exception.getStatusWithCode().getHttpStatus())
            .body(ApiResponse.errorResponse(
                exception.getStatusWithCode().getHttpStatus().value(),
                exception.getStatusWithCode().getMessage()
            ));
    }
}