package com.kakao.saramaracommunity.vote.dto.business.response;

import java.util.Map;

public record VotesReadInBoardResponse(
        Long boardId,
        Boolean isVoted, // 투표 진행 상태
        Long totalVotes,
        Map<String, Long> voteCounts // 이미지 경로, 투표수
) {

    public static VotesReadInBoardResponse from(
            Long boardId,
            Boolean isVoted,
            Long totalVotes,
            Map<String, Long> voteCounts
    ) {
        return new VotesReadInBoardResponse(boardId, isVoted, totalVotes, voteCounts);
    }

}
