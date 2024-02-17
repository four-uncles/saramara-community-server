package com.kakao.saramaracommunity.comment.dto.business.request;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;
import java.util.Objects;
import lombok.Builder;

@Builder
public record CommentCreateServiceRequest(
        Long memberId,
        Long boardId,
        String content
) {

    public Comment toEntity(Member member, Board board) {
        return Comment.of(member, board, content);
    }

}
