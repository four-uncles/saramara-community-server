package com.kakao.saramaracommunity.handler;

import static org.springframework.http.HttpStatus.*;

import java.sql.Timestamp;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kakao.saramaracommunity.common.dto.ErrorDto;
import com.kakao.saramaracommunity.exception.DuplicateMemberException;
import com.kakao.saramaracommunity.exception.NotFoundMemberException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
   /* @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = { DuplicateMemberException.class })
    @ResponseBody
    protected ErrorDto conflict(RuntimeException ex, WebRequest request) {

        return new ErrorDto(timestamp,CONFLICT.value(),ex.getMessage(), request.getContextPath());
    }*/

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = { DuplicateMemberException.class })
    @ResponseBody
    protected ErrorDto conflict(RuntimeException ex, HttpServletRequest request) {
        log.info(ex.getMessage());
        log.info(ex);
        ErrorDto errorDto = new ErrorDto(timestamp, CONFLICT.value(),ex.getMessage(), request.getRequestURI());

        return errorDto;
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = { NotFoundMemberException.class, AccessDeniedException.class })
    @ResponseBody
    protected ErrorDto forbidden(RuntimeException ex, HttpServletRequest request) {
        return new ErrorDto(timestamp, FORBIDDEN.value(), ex.getMessage(), request.getRequestURI());
    }
}
