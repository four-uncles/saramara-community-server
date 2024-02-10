package com.kakao.saramaracommunity.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberException extends RuntimeException {

    private final MemberErrorCode memberErrorCode;
}
