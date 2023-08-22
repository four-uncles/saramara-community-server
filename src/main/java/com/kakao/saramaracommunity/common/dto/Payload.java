package com.kakao.saramaracommunity.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * 공통 ResponseDTO로 사용할 Payload 클래스입니다.
 * 응답데이터로 줄 때 null이 발생하는 것들은 응답데이터로 던지지 못하도록 @JsonInclude를 사용하였습니다.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Payload<T> {

    private Integer status;

    private String message;

    private T data;

    /**
     * exceptionHandler에 응답 데이터로 사용할 errorPayload 메서드입니다.
     *
     * @param status - 상태값
     * @param message - 오류 메세지
     * @return Payload
     */
    public static <T> Payload<T> errorPayload(final Integer status, final String message) {

        return Payload.<T>builder()
                .status(status)
                .message(message)
                .build();
    }

    /**
     * Controller에 응답 데이터로 사용할 successPayload 메서드입니다.
     *
     * @param status - 상태값
     * @param message - 사용에 따라 다름
     * @param data - 던져줄 데이터
     * @return Payload
     */
    public static <T> Payload<T> successPayload(final Integer status, final String message, final T data) {

        return Payload.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}