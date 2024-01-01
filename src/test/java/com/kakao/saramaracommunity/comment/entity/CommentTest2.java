package com.kakao.saramaracommunity.comment.entity;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.kakao.saramaracommunity.fixture.BoardFixture.createBoard;
import static com.kakao.saramaracommunity.fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;

class CommentTest2 {

    @DisplayName("댓글 작성시 투표하지 않아도 정상적으로 등록된다.")
    @Test
    void createCommentWhenPickIsEmpty() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member boardWriter = createMember("owner@test.com", "owner");
        Member commentWriter = createMember("guest@test.com", "guest");
        Board board = createBoard(boardWriter, attachPaths);

        // when
        Comment comment = createCommentWithoutPick(commentWriter, board);

        // then
        assertThat(comment.getPick()).isZero();
    }

    @DisplayName("댓글 작성시 첨부파일을 업로드하지 않아도 정상적으로 등록된다.")
    @Test
    void createCommentWhenAttachmentIsEmpty() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member boardWriter = createMember("owner@test.com", "owner");
        Member commentWriter = createMember("guest@test.com", "guest");
        Board board = createBoard(boardWriter, attachPaths);

        // when
        Comment comment = createCommentWithoutAttachment(commentWriter, board);

        // then
        assertThat(comment.getContent()).isEqualTo("저는 수면잠옷을 더 추천드려요..!");
    }

    @DisplayName("댓글 수정시 투표를 변경할 수 있다.")
    @Test
    void changeCommentPick() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member boardWriter = createMember("owner@test.com", "owner");
        Member commentWriter = createMember("guest@test.com", "guest");
        Board board = createBoard(boardWriter, attachPaths);
        Comment comment = createComment(commentWriter, board, 1);

        // when
        comment.changeComment("저는 수면잠옷을 더 추천드려요..!", 3, "");

        // then
        assertThat(comment.getPick()).isEqualTo(3);
    }

    private Comment createComment(Member commentWriter, Board board, int pick) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .pick(pick)
                .attachmentUrl("attach-url")
                .build();
    }

    private Comment createCommentWithoutPick(Member commentWriter, Board board) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .attachmentUrl("attach-url")
                .build();
    }

    private Comment createCommentWithoutAttachment(Member commentWriter, Board board) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 수면잠옷을 더 추천드려요..!")
                .pick(1)
                .build();
    }

}