package com.kakao.saramaracommunity.comment.service.dto.request;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;
import java.util.Objects;
import lombok.Builder;

@Builder
public record CommentCreateServiceRequest(Long memberId, Long boardId, String content) {

    public CommentCreateServiceRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
        Objects.requireNonNull(boardId, " 게시글 정보는 필수 입니다.");
        Objects.requireNonNull(content, " 댓글 내용은 필수 입니다.");
    }

    public Comment toEntity(Member member, Board board) {
        return Comment.of(member, board, content);
    }

}
