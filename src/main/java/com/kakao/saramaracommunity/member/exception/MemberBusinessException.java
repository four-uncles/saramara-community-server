package com.kakao.saramaracommunity.member.exception;

import lombok.Getter;

@Getter
public class MemberBusinessException extends RuntimeException {

    private final MemberErrorCode memberErrorCode;

    public MemberBusinessException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.getMessage());
        this.memberErrorCode = memberErrorCode;
    }
}
