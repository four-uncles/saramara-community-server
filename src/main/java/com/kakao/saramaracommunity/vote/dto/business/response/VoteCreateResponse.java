package com.kakao.saramaracommunity.vote.dto.business.response;

import com.kakao.saramaracommunity.vote.entity.Vote;
import lombok.Builder;

@Builder
public record VoteCreateResponse(
        String voter,
        Long boardId,
        Long boardImageId
) {

    public static VoteCreateResponse of(Vote vote) {
        return VoteCreateResponse.builder()
                .voter(vote.getMember().getNickname())
                .boardId(vote.getBoard().getId())
                .boardImageId(vote.getBoardImage().getId())
                .build();
    }

}
