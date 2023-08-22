package com.kakao.saramaracommunity.common.exception;

import com.kakao.saramaracommunity.common.dto.Payload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * GlobalExceptionHandler: 도메인과 관련된 예외가 아닌 나머지 예외를 핸들링하는 클래스입니다.
 * 기본적인 예외를 처리하기 위해 ResponseEntityExceptionHandler를 상속 받습니다.
 * 400(유효성 검사), 404, 500 에러에 대한 예외를 핸들링합니다.
 * 가장 낮은 우선순위로 예외를 핸들링합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@RestControllerAdvice
@Order
@Log4j2
public class GlobalExceptionHandler {

    /**
     * 유효성 검사를 진행할 @Valid 어노테이션이 붙은 요청 DTO 클래스에서 유효성 검사가 실패할 경우 발생하는 예외를 핸들링하는 메서드입니다.
     * 메시지의 경우 유효성 검사 결과 객체에서 필드 오류 정보를 찾은 후, 해당 예외의 기본 메시지를 추출하여 문자열로 반환합니다.
     *
     * ResponseEntity를 사용한 이유는 응답 상태 코드, 헤더 등을 더 세밀하게 제어할 수 있기 때문입니다.
     * 또한, HTTP 응답을 더 세부적으로 커스터마이징할 수 있기 때문에 유연성이 높습니다.
     *
     * @param exception 유효성 검증 실패 시 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Payload> handleMethodArgumentNotValidException (MethodArgumentNotValidException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Payload.errorPayload(
                                HttpStatus.BAD_REQUEST.value(),
                                exception.getBindingResult().getFieldErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toList().toString()
                        )
                );

    }

    /**
     * 서버에서 요청한 리소스를 찾을 수 없는 예외를 핸들링하는 메서드입니다.
     *
     * @param exception 요청에 대한 리소르를 찾지 못할 경우 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Payload> handleNotFoundException (
            NoHandlerFoundException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        Payload.errorPayload(
                                HttpStatus.NOT_FOUND.value(),
                                exception.getMessage()
                        )
                );

    }

    /**
     * 서버 내부에서 문제 발생시 예외를 핸들링하는 메서드입니다.
     * 어떤 문제인지 파악하기 쉽도록 request의 일부와 exception 스택 트레이스 로그를 출력합니다.
     *
     * @param exception 서버 문제 발생시 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Payload> handleException (
            Exception exception,
            HttpServletRequest request
    ) {

        log.error("[ERROR] 서버 내부에서 문제가 발생했습니다. method: {}, requestURI: {}, msg: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        log.error("{}", (Object) exception.getStackTrace());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Payload.errorPayload(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                exception.getMessage()
                        )
                );

    }

}
