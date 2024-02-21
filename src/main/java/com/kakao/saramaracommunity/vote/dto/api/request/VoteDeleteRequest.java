package com.kakao.saramaracommunity.vote.dto.api.request;

import com.kakao.saramaracommunity.vote.dto.business.request.VoteDeleteServiceRequest;
import jakarta.validation.constraints.NotNull;

public record VoteDeleteRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId
) {

    public VoteDeleteServiceRequest toServiceRequest() {
        return VoteDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
