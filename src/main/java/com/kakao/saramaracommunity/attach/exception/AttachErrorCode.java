package com.kakao.saramaracommunity.attach.exception;

import com.kakao.saramaracommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * AttachRequest: Attach(첨부 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@RequiredArgsConstructor
@Getter
public enum AttachErrorCode implements ErrorCode {

    /**
     * Attach Code
     * ATTACH_IMAGE_LIMIT: 업로드할 수 있는 이미지 개수의 범위를 벗어날 경우 400 에러코드를 반환
     * ATTACH_INSERT_FAILED: ATTACH 테이블에 데이터를 저장할 때 문제가 발생할 경우 400 에러코드를 반환
     * ATTACH_UPDATE_FAILED: ATTACH 테이블에 데이터를 수정할 때 문제가 발생할 경우 400 에러코드를 반환
     * ATTACH_DELETE_FAILED: ATTACH 테이블에 데이터를 삭제할 때 문제가 발생할 경우 400 에러코드를 반환
     * ATTACH_NOT_FOUND: ATTACH 객체가 비어있을 경우 404 에러코드를 반환
     * ATTACH_AWS_S3_UPLOAD_FAILED: AWS S3 Bucket으로 업로드를 실패할 경우 500 에러코드를 반환
     */
    ATTACH_IMAGE_RANGE_OUT(HttpStatus.BAD_REQUEST, "이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다."),

    ATTACH_INSERT_FAILED(HttpStatus.BAD_REQUEST, "DB에 데이터를 저장할 때 문제가 발생했습니다."),

    ATTACH_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "DB에 데이터를 수정할 때 문제가 발생했습니다."),

    ATTACH_DELETE_FAILED(HttpStatus.BAD_REQUEST, "DB에 데이터를 삭제할 때 문제가 발생했습니다."),

    ATTACH_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지(데이터)를 찾을 수 없습니다."),

    ATTACH_AWS_S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 버킷으로 이미지를 업로드하던 중 문제가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

}
