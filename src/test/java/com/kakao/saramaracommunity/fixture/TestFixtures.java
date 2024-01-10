package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public class TestFixtures {

    public static Member createMember(String email, String nickname) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password("test")
                // .type(Type.LOCAL)
                .build();
    }

    public static Board createBoard(Member member, List<String> attachPaths) {
        return Board.builder()
                .title("집에서 잠옷으로 입을 옷을 골라주세요.")
                .content("content")
                .member(member)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();
    }

    public static Comment createComment(Member member, Board board, int pick) {
        return Comment.builder()
                .member(member)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .pick(pick)
                .attachmentUrl("attach-url")
                .build();
    }
}
