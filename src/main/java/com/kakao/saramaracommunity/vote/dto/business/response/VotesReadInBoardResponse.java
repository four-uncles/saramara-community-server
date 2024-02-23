package com.kakao.saramaracommunity.vote.dto.business.response;

import java.util.Map;

public record VotesReadInBoardResponse(
        Long boardId,
        Long totalVotes,
        Map<String, Long> voteCounts // 이미지 경로, 투표수
) {

    public static VotesReadInBoardResponse from(
            Long boardId,
            Long totalVotes,
            Map<String, Long> voteCounts
    ) {
        return new VotesReadInBoardResponse(boardId, totalVotes, voteCounts);
    }

}
