package com.kakao.saramaracommunity.comment.entity;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentSetUpTest {

    private Member commentWriter;
    private Board board;

    @BeforeEach
    void setUp() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member boardWriter = Member.builder()
                .email("owner@test.com")
                .nickname("owner")
                .password("test")
                .type(Type.LOCAL)
                .build();
        commentWriter = Member.builder()
                .email("guest@test.com")
                .nickname("guest")
                .password("test")
                .type(Type.LOCAL)
                .build();
        board = Board.builder()
                .title("집에서 잠옷으로 입을 옷을 골라주세요.")
                .content("content")
                .member(boardWriter)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();
    }

    @DisplayName("댓글 작성시 투표하지 않아도 정상적으로 등록된다.")
    @Test
    void createCommentWhenPickIsEmpty() {
        // when
        Comment comment = Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .build();

        // then
        assertThat(comment.getPick()).isZero();
    }

    @DisplayName("댓글 작성시 첨부파일을 업로드하지 않아도 정상적으로 등록된다.")
    @Test
    void createCommentWhenAttachmentIsEmpty() {
        // when
        Comment comment = Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .pick(1)
                .build();

        // then
        assertThat(comment.getContent()).isEqualTo("저는 수면잠옷을 더 추천드려요..!");
    }

    @DisplayName("댓글 수정시 투표를 변경할 수 있다.")
    @Test
    void changeCommentPick() {
        // changeComment for given
        Comment comment = Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .pick(1)
                .build();

        // when
        comment.changeComment("저는 수면잠옷을 더 추천드려요..!", 3, "");

        // then
        assertThat(comment.getPick()).isEqualTo(3);
    }

}