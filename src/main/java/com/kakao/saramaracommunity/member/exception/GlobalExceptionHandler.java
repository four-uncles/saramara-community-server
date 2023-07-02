package com.kakao.saramaracommunity.member.exception;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.ErrorCode;
import com.kakao.saramaracommunity.member.service.MemberServiceMethod;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MemberResDto> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception, HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

		BindingResult bindingResult = exception.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		String errorMessage;
		HttpStatus status;

		log.error(fieldErrors);

		if (!fieldErrors.isEmpty()) {
			FieldError fieldError = fieldErrors.get(0);
			String fieldErrorCode = fieldError.getCode();

			if (fieldError.getField().equals("email")) {
				errorCode = ErrorCode.INVALID_INPUT_EMAIL;
				log.warn(fieldError.getCode());
				if (fieldErrorCode.equals("NotNull") || fieldErrorCode.equals("NotBlank")) {
					errorMessage = ErrorCode.INVALID_INPUT_EMAIL.getDescription();
					status = ErrorCode.INVALID_INPUT_EMAIL.getHttpStatus();
				} else if (fieldErrorCode.equals("Size")) {
					errorMessage = fieldError.getDefaultMessage();
					status = ErrorCode.INVALID_INPUT_VALUE.getHttpStatus();
				} else if (fieldErrorCode.equals("Pattern")) {
					log.warn(fieldErrorCode.equals("Pattern"));
					errorMessage = ErrorCode.INVALID_INPUT_EMAIL.getDescription();
					status = ErrorCode.INVALID_INPUT_EMAIL.getHttpStatus();
				} else {
					errorMessage = ErrorCode.INTERNAL_SERVER_ERROR.getDescription();
					status = ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
				}
			} else if (fieldError.getField().equals("password")) {
				errorCode = ErrorCode.INVALID_INPUT_PW;
				log.warn(fieldError.getCode());
				if (fieldErrorCode.equals("NotNull") || fieldErrorCode.equals("NotBlank")) {
					errorMessage = ErrorCode.INVALID_INPUT_PW.getDescription();
					status = ErrorCode.INVALID_INPUT_PW.getHttpStatus();
				} else if (fieldErrorCode.equals("Pattern")) {
					errorMessage = ErrorCode.INVALID_INPUT_PW.getDescription();
					status = ErrorCode.INVALID_INPUT_PW.getHttpStatus();
				} else {
					errorMessage = ErrorCode.INTERNAL_SERVER_ERROR.getDescription();
					status = ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
				}
			} else {
				errorMessage = ErrorCode.INVALID_INPUT_VALUE.getDescription();
				status = ErrorCode.INVALID_INPUT_VALUE.getHttpStatus();
			}

			MemberResDto response = MemberResDto.builder()
				.success(false)
				.status(errorCode)
				.data(errorMessage)
				.build();

			return new ResponseEntity<>(response, headers, status);
		}

		errorMessage = ErrorCode.INTERNAL_SERVER_ERROR.getDescription();
		status = ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
		MemberResDto response = MemberResDto.builder()
			.success(false)
			.status(ErrorCode.INTERNAL_SERVER_ERROR)
			.data(errorMessage)
			.build();

		return new ResponseEntity<>(response, headers, status);
	}
}
