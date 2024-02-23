package com.kakao.saramaracommunity.vote.dto.api.request;

import com.kakao.saramaracommunity.vote.dto.business.request.VoteCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record VoteCreateRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId,
        @NotNull(message = "게시글 정보는 필수 입니다.") Long boardId,
        @NotNull(message = "투표할 이미지 정보는 필수 입니다.") Long boardImageId
) {

    public VoteCreateServiceRequest toServiceRequest() {
        return VoteCreateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .boardImageId(boardImageId)
                .build();
    }

}
