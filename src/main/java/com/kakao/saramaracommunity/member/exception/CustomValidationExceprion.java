package com.kakao.saramaracommunity.member.exception;

import com.kakao.saramaracommunity.member.dto.ErrorCode;

import lombok.Getter;

@Getter
public class CustomValidationExceprion extends RuntimeException{
	private final ErrorCode errorCode;
	public CustomValidationExceprion(ErrorCode errorCode) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
	}
}
