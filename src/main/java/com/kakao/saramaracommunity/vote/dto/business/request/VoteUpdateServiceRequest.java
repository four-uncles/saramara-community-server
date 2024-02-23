package com.kakao.saramaracommunity.vote.dto.business.request;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import lombok.Builder;

@Builder
public record VoteUpdateServiceRequest(
        Long memberId,
        BoardImage boardImage
) {
}
