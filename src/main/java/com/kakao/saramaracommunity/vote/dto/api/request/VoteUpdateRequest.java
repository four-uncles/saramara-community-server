package com.kakao.saramaracommunity.vote.dto.api.request;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record VoteUpdateRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId,
        @NotNull(message = "투표할 이미지 정보는 필수 입니다.") BoardImage boardImage
) {

    public VoteUpdateServiceRequest toServiceRequest() {
        return VoteUpdateServiceRequest.builder()
                .memberId(memberId)
                .boardImage(boardImage)
                .build();
    }

}
