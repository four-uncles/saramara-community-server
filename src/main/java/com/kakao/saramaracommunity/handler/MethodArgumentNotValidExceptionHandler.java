/*
package com.kakao.saramaracommunity.handler;

import static org.springframework.http.HttpStatus.*;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kakao.saramaracommunity.common.dto.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors, request, ex);
    }

    private ErrorDto processFieldErrors(List<FieldError> fieldErrors, HttpServletRequest request, MethodArgumentNotValidException ex) {
        Timestamp nowExp = new Timestamp(System.currentTimeMillis());
        ErrorDto errorDTO = new ErrorDto(nowExp, BAD_REQUEST.value(),"Parameter Wrong", request.getRequestURI());
        for (FieldError fieldError: fieldErrors) {
            errorDTO.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorDTO;
    }
}*/
