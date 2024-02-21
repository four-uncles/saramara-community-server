package com.kakao.saramaracommunity.vote.exception;

import lombok.Getter;

@Getter
public class VoteBusinessException extends RuntimeException {

    private final VoteErrorCode statusWithCode;

    public VoteBusinessException(VoteErrorCode statusWithCode) {
        super(statusWithCode.getMessage());
        this.statusWithCode = statusWithCode;
    }

}
