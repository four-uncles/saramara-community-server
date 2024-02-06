package com.kakao.saramaracommunity.member.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kakao.saramaracommunity.board.exception.BoardNotFoundException;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.exception.MemberException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MemberControllerAdvice {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ApiResponse> handleBoardNotFound(MemberException exception) {
		return ResponseEntity
			.status(exception.getMemberErrorCode().getHttpStatus())
			.body(ApiResponse.errorResponse(
				exception.getMemberErrorCode().getHttpStatus().value(),
				exception.getMemberErrorCode().getMessage()
			));
	}
}
