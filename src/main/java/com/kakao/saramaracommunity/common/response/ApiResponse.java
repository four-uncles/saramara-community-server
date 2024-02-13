package com.kakao.saramaracommunity.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 공통 ResponseDTO로 사용할 ApiResponse 클래스입니다.
 * 응답데이터로 줄 때 null이 발생하는 것들은 응답데이터로 던지지 못하도록 @JsonInclude를 설정했습니다.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final int code;

    private final String message;

    private final T data;

    private final LocalDateTime timestamp = LocalDateTime.now();

    @Builder
    private ApiResponse(final int code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * API 요청 성공시, 응답 데이터로 사용할 successResponse 메서드입니다.
     *
     * @param httpStatus - HttpStatus Code
     * @param message - 응답 메세지
     * @param data - 던져줄 데이터
     */
    public static <T> ApiResponse<T> successResponse(
            final HttpStatus httpStatus,
            final String message,
            final T data
    ) {
        return ApiResponse.<T>builder()
                .code(httpStatus.value())
                .message(message)
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> successResponse(
            final HttpStatus httpStatus,
            final String message
    ) {
        return ApiResponse.<T>builder()
                .code(httpStatus.value())
                .message(message)
                .build();
    }

    /**
     * 요청 수행 중, 예외 발생시 응답 데이터로 사용할 errorResponse 메서드입니다.
     *
     * @param code - HttpStatus Code
     * @param message - 오류 메세지
     */
    public static <T> ApiResponse<T> errorResponse(
            final int code,
            final String message
    ) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

}
