package com.kakao.saramaracommunity.member.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
/*
@Valid 어노테이션의 유효성을 통과하지 못하는 경우 MethodArgumentNotValidException 을 발생하므로
@ExceptionHandler(MethodArgumentNotValidException.class) 으로 @Valid의 Exception을 처리하겠다고 선언
Exception에 대한 결과를 ErrorCode 클래스를 이용해서 MemberResDto를 통해 Client에 응답하기 위함이다.
*/
@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MemberResDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		ValidExceptionHandlingMethod validExceptionHandlingMethod = new ValidExceptionHandlingMethod();

		/*
		@Valid로 부터 발생되는 MethodArgumentNotValidException 을 exception 이라는 객체로 받아와
		Binding 및 유효성 검증 결과를 저장하는 인터페이스 BindingResult 형의 객체에 해당 Exception 검증결과를 getBindingResult()로 받아와
		유혀성 검증에서 발생된 Exception 을 FieldError 형태의 List로 가지고 있어서 해당되는 Exception에 접근할 수 있도록 했다.
		*/
		BindingResult bindingResult = exception.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		MemberResDto response = MemberResDto.builder()
			.success(false)
			.status(ErrorCode.INTERNAL_SERVER_ERROR)
			.build();

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		// MethodArgumentNotValidException 에 대한 Exception 이 감지된 경우를 의미
		// 아래의 경우에 해당되는 Exception이 아닌 경우에 해당 handleMethodArgumentNotValidException 메서드가 불러졌다면, INTERNAL_SERVER_ERROR로 처리
		if (!fieldErrors.isEmpty()) {
			FieldError fieldError = fieldErrors.get(0);
			String fieldErrorCode = fieldError.getCode();

			// @Valid Exception 의 필드가 이메일인 경우
			if (fieldError.getField().equals("email")) {
				response = validExceptionHandlingMethod.emailValidExceptionHandling(fieldError, fieldErrorCode);

				// response 는 MemberResDto로 해당 클래스의 status는 ErrorCode 클래스의 정의된 status를 의미하며 이를 Getter 로 가져와서
				// ErrorCode에 정의된 getHttpStatus() 메서드로 response(현재 Exception에 상응하는 HttpStatus를 가져온다.
				status = response.getStatus().getHttpStatus();

				return new ResponseEntity<>(response, status);
			}
			// @Valid Exception 의 필드가 비밀번호인 경우
			else if (fieldError.getField().equals("password")) {
				response = validExceptionHandlingMethod.passwordValidExceptionHandling(fieldErrorCode);
				status = response.getStatus().getHttpStatus();

				return new ResponseEntity<>(response, status);
			}
			// @Valid Exception 의 필드가 닉네임인 경우
			else if (fieldError.getField().equals("nickname")) {
				response = validExceptionHandlingMethod.nicknameValidExceptionHandling(fieldErrorCode);
				status = response.getStatus().getHttpStatus();

				return new ResponseEntity<>(response, status);
			}
			// 위의 경우의 에러가 벗어난 handleMethodArgumentNotValidException 처리
			else {
				log.error(fieldError.getRejectedValue());
			}
		}


		return new ResponseEntity<>(response, status);
	}
}

