package com.kakao.saramaracommunity.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * 파일 목록의 유효성 검사를 진행하는 클래스입니다.
 * ConstraintValidator의 구현체로 isValid 메서드를 재정의하여 유효성 검증 여부를 반환합니다.
 */
public class FileListValidator implements ConstraintValidator<ValidFileList, List<MultipartFile>> {

    @Value("${image-rule.max}")
    private int MAX_IMAGE_COUNT;

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (isNull(files)) {
            addConstraintViolation(context, "요청으로 받은 이미지 목록 필드가 누락되었습니다.");
            return false;
        }
        if (files.isEmpty()) {
            addConstraintViolation(context, "이미지는 필수로 업로드해야 합니다.");
            return false;
        }
        if (files.size() > MAX_IMAGE_COUNT) {
            addConstraintViolation(context, "이미지는 최대 5장까지만 업로드할 수 있습니다.");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
