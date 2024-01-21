package com.kakao.saramaracommunity.comment.service.dto.request;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.Builder;

@Builder
public record CommentCreateServiceRequest(Long memberId, Long boardId, String content) {

    public static CommentCreateServiceRequest of(Long memberId, Long boardId, String content) {
        return CommentCreateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
    }

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
    }

}
