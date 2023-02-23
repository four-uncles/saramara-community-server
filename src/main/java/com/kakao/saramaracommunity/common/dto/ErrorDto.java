package com.kakao.saramaracommunity.common.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorDto {
    private final Timestamp timestamp;
    private final int status;
    private final String message;
    private final String path;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public void addFieldError(String objectName, String path, String message) {
        FieldError error = new FieldError(objectName, path, message);
        fieldErrors.add(error);
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
