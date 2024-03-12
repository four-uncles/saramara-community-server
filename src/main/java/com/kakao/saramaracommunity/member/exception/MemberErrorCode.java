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

    INVALID_EMAIL(BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME(BAD_REQUEST, "닉네임은 최소 1자 이상, 최대 10자 이하까지 등록할 수 있습니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호는 최소 8자 이상, 최대 16자 이하로 숫자와 특수문자를 반드시 포함해야 합니다."),
    WRONG_PASSWORD(BAD_REQUEST, "비밀번호가 틀렸습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다."),
    UNAUTHORIZED_TO_MEMBER(FORBIDDEN, "권한이 없는 사용자입니다.");

    private final HttpStatus httpStatus;

    private final String message;

}
