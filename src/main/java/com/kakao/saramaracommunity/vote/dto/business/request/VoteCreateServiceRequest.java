package com.kakao.saramaracommunity.vote.dto.business.request;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.vote.entity.Vote;
import lombok.Builder;

/**
 * - `memberId`: 투표자의 고유 식별자
 * - `boardId`: 게시글의 고유 식별자
 * - `boardImageId`: 투표할 이미지의 고유 식별자
 */
@Builder
public record VoteCreateServiceRequest(
        Long memberId,
        Long boardId,
        Long boardImageId
) {

    public Vote toEntity(Member member, Board board, BoardImage boardImage) {
        return Vote.of(member, board, boardImage);
    }

}
