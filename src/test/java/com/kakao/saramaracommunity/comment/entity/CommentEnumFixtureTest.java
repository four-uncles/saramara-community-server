package com.kakao.saramaracommunity.comment.entity;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.kakao.saramaracommunity.fixture.BoardEnumFixture.BOARD_CHOICE_WRITER_SONNY;
import static com.kakao.saramaracommunity.fixture.BoardEnumFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_SONNY;
import static org.assertj.core.api.Assertions.assertThat;

class CommentEnumFixtureTest {

    @DisplayName("댓글 작성시 투표하지 않아도 정상적으로 등록된다.")
    @Test
    void createCommentWhenPickIsEmpty() {
        // given
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Board BOARD_WRITER_LANGO = BOARD_VOTE_WRITER_LANGO.createBoard();

        // when
        Comment comment = createCommentWithoutPick(COMMENT_WRITER_SONNY, BOARD_WRITER_LANGO);

        // then
        assertThat(comment.getPick()).isZero();
    }

    @DisplayName("댓글 작성시 이미지를 첨부하지 않아도 정상적으로 작성할 수 있다.")
    @Test
    void createCommentWhenImageIsEmpty() {
        // given
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Board BOARD_WRITER_LANGO = BOARD_VOTE_WRITER_LANGO.createBoard();

        // when
        Comment comment = createCommentWithoutAttachment(COMMENT_WRITER_SONNY, BOARD_WRITER_LANGO);

        // then
        assertThat(comment.getContent()).isEqualTo("1번 잠옷이 더 귀엽네요.");
    }

    @DisplayName("댓글 수정시 내용 변경과 함께 이미지를 첨부할 수 있다.")
    @Test
    void changeCommentContentWithImageUpload() {
        // given
        Member COMMENT_WRITER_LANGO = NORMAL_MEMBER_LANGO.createMember();
        Board BOARD_WRITER_SONNY = BOARD_CHOICE_WRITER_SONNY.createBoard();

        Comment comment = createComment(COMMENT_WRITER_LANGO, BOARD_WRITER_SONNY, 1);

        // when
        comment.changeComment("저는 발마칸 코트 너무 이쁜 것 같아요. 구매 추천드립니다!!", "image-02-update");

        // then
        assertThat(comment.getContent()).isEqualTo("저는 발마칸 코트 너무 이쁜 것 같아요. 구매 추천드립니다!!");
        assertThat(comment.getAttachmentUrl()).isEqualTo("image-02-update");
    }

    private Comment createCommentWithoutPick(Member commentWriter, Board board) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("1번 잠옷이 더 귀엽네요.")
                .attachmentUrl("image-01")
                .build();
    }

    private Comment createCommentWithoutAttachment(Member commentWriter, Board board) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("1번 잠옷이 더 귀엽네요.")
                .pick(1)
                .build();
    }

    private Comment createComment(Member commentWriter, Board board, int pick) {
        return Comment.builder()
                .member(commentWriter)
                .board(board)
                .content("저는 발마칸 코트를 입어보질 않았는데, 이번 주에 쇼핑가서 입어보고 댓글로 남겨놓겠습니다!")
                .pick(pick)
                .attachmentUrl("image-01")
                .build();
    }

}