package com.kakao.saramaracommunity.member.exception;

import org.springframework.http.HttpStatus;

import com.kakao.saramaracommunity.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_TO_MEMBER(FORBIDDEN, "권한이 없는 사용자입니다."),
    WRONG_PASSWORD(BAD_REQUEST, "입력하신 비밀번호를 다시 확인해 주세요.");

    private final HttpStatus httpStatus;

    private final String message;

}
