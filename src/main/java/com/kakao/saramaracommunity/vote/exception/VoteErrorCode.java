package com.kakao.saramaracommunity.vote.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.kakao.saramaracommunity.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum VoteErrorCode implements ErrorCode {

    VOTE_NOT_FOUND(NOT_FOUND, "존재하지 않는 투표입니다.");

    private final HttpStatus httpStatus;

    private final String message;

}
