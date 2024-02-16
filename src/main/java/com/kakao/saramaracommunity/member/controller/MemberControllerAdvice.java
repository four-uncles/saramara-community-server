package com.kakao.saramaracommunity.member.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MemberControllerAdvice {

	@ExceptionHandler(MemberBusinessException.class)
	public ResponseEntity<ApiResponse> handleBoardNotFound(MemberBusinessException exception) {
		return ResponseEntity
			.status(exception.getMemberErrorCode().getHttpStatus())
			.body(ApiResponse.errorResponse(
				exception.getMemberErrorCode().getHttpStatus().value(),
				exception.getMemberErrorCode().getMessage()
			));
	}
}
