package com.kakao.saramaracommunity.attach.controller;

import com.kakao.saramaracommunity.attach.exception.AttachNotFoundException;
import com.kakao.saramaracommunity.attach.exception.ImageUploadOutOfRangeException;
import com.kakao.saramaracommunity.common.dto.Payload;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * AttachControllerAdvice: Attach(첨부 이미지) 관련 예외를 처리할 클래스
 * 가장 높은 우선순위로 예외를 핸들링합니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Log4j2
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AttachControllerAdvice {

    /**
     * 업로드한 이미지의 개수가 정한 범위를 벗어났을 경우 발생하는 예외를 핸들링하는 메서드입니다.
     * 이미지 개수 범위는 0.0.1 버전 기준 1장부터 5장까지입니다.
     *
     * @param exception 이미지 개수 범위를 벗어났을 경우
     * @return Status 코드와 예외 메시지 반환
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ImageUploadOutOfRangeException.class)
    public ResponseEntity<Payload> handleImageUploadOutOfRange(ImageUploadOutOfRangeException exception) {

        log.error("[ImageUploadOutOfRangeException]");

        return ResponseEntity
                .status(exception.getStatusWithCode().getHttpStatus())
                .body(
                        Payload.errorPayload(
                            exception.getStatusWithCode().getHttpStatus().value(),
                            exception.getStatusWithCode().getMessage()
                        )
                );
    }

    /**
     * 원하는 이미지를 찾지 못했을 때 발생하는 예외를 핸들링하는 메서드입니다.
     *
     * @param exception 이미지를 찾지 못했을 경우
     * @return Status 코드와 예외 메시지 반환
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AttachNotFoundException.class)
    public ResponseEntity<Payload> handleAttachNotFound(AttachNotFoundException exception) {

        log.error("[AttachNotFoundException]");

        return ResponseEntity
                .status(exception.getStatusWithCode().getHttpStatus())
                .body(
                        Payload.errorPayload(
                                exception.getStatusWithCode().getHttpStatus().value(),
                                exception.getStatusWithCode().getMessage()
                        )
                );
    }




}
