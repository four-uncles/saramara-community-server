package com.kakao.saramaracommunity.member.exception;


import org.springframework.validation.FieldError;

import com.kakao.saramaracommunity.member.dto.MemberErrorCode;
import com.kakao.saramaracommunity.member.dto.MemberResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ValidExceptionHandlingMethod {

	// ErrorCode 기본값 초기화
	MemberErrorCode memberErrorCode;
	String errorMessage;

	/*
	아래는 @Valid를 통과하지 못한 각 field(Dto 의 각 필드)를 접근해서 예외가 발생한 첫 번째 경우에 대한 Exception을 응답하도록 하는 매서드들로
	fieldErrorCode 를 매개변수로 받는 이유는 아래와 같다.

	 Dto 에서 @Valid를 수행하는 중 Exception이 발생한 필드에 접근하기 위해 매개변수로 받아온다.
	 해당 필드에서 통과하지 못한 유효성 (Ex> @NotNull, @Pattern, @Size 등)에 대해 접근하기 위해 매개변수로 받아온다.
	 -> Ex> SignUpDto의 email, nickname, password의 필드를 접근하기 위함이다.

	 이후 해당되는 경우에 따라서 ErrorCode에 정의된 에러 코드갑과 그에 상응하는 HttpStatus 를 기반으로 결과를 만들어 반환한다.
	*/

	// Email @Valid 유효성 검사 실패한 경우에 발생할 수 있는 Exception 을 다루는 메서드
	// Email 의 최대, 최소 길이를 @Size로 설정해서 @Size에 대한 Exception은 ErrorCode에 정의하지 않아서 기본 Message로 지정한 값으로 대체하기 위해서
	// fieldError 를 받아온다.
	public MemberResDto emailValidExceptionHandling(FieldError fieldError, String fieldErrorCode){
			memberErrorCode = MemberErrorCode.INVALID_INPUT_EMAIL;

			// @NotNull, @NotBlank 에 대한 Exception
			if (fieldErrorCode.equals("NotNull") || fieldErrorCode.equals("NotBlank")) {
				errorMessage = MemberErrorCode.INVALID_INPUT_EMAIL.getDescription();
			}
			// @Size에 대한 Exception
			else if (fieldErrorCode.equals("Size")) {
				errorMessage = fieldError.getDefaultMessage();
			}
			// Email 형식이 올바른지 검증하는 것에 대한 Exception
			else if (fieldErrorCode.equals("Pattern")) {
				errorMessage = MemberErrorCode.INVALID_INPUT_EMAIL.getDescription();
			}
			// 그외 알 수 없는 경우의 Exception 처리
			else {
				errorMessage = MemberErrorCode.INTERNAL_SERVER_ERROR.getDescription();
			}
			MemberResDto response = MemberResDto.builder()
				.success(false)
				.data(errorMessage)
				.memberErrorCode(memberErrorCode)
				.build();
			return response;
	}

	// Password @Valid 유효성 검사 실패한 경우에 발생할 수 있는 Exception 을 다루는 메서드
	public MemberResDto passwordValidExceptionHandling(String fieldErrorCode){
		memberErrorCode = MemberErrorCode.INVALID_INPUT_PW;

		// @NotNull, @NotBlank 에 대한 Exception
		if (fieldErrorCode.equals("NotNull") || fieldErrorCode.equals("NotBlank")) {
			errorMessage = MemberErrorCode.INVALID_INPUT_PW.getDescription();
		}
		// 올바른 비밀번호 형식 @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}") 를 통과하지 못한 경우
		 else if (fieldErrorCode.equals("Pattern")) {
			errorMessage = MemberErrorCode.INVALID_INPUT_PW.getDescription();
		}
		// 그외 알 수 없는 경우의 Exception 처리
		 else {
			errorMessage = MemberErrorCode.INTERNAL_SERVER_ERROR.getDescription();
		}
		MemberResDto response = MemberResDto.builder()
			.success(false)
			.data(errorMessage)
			.memberErrorCode(memberErrorCode)
			.build();
		return response;
	}

	// Nickname @Valid 유효성 검사 실패한 경우에 발생할 수 있는 Exception 을 다루는 메서드
	public MemberResDto nicknameValidExceptionHandling(String fieldErrorCode){
		memberErrorCode = MemberErrorCode.INVALID_INPUT_NICKNAME;

		// @NotNull, @NotBlank 에 대한 Exception
		if (fieldErrorCode.equals("NotNull") || fieldErrorCode.equals("NotBlank")) {
			errorMessage = MemberErrorCode.INVALID_INPUT_NICKNAME.getDescription();
		}
		// 올바른 닉네임 형식 @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9-_]{2,10}$")
		else if (fieldErrorCode.equals("Pattern")) {
			errorMessage = MemberErrorCode.INVALID_INPUT_NICKNAME.getDescription();
		}
		// 그외 알 수 없는 경우의 Exception 처리
		else {
			errorMessage = MemberErrorCode.INTERNAL_SERVER_ERROR.getDescription();
		}
		MemberResDto response = MemberResDto.builder()
			.success(false)
			.data(errorMessage)
			.memberErrorCode(memberErrorCode)
			.build();
		return response;
	}
}
