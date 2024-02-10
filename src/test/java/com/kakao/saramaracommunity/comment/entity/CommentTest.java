package com.kakao.saramaracommunity.comment.entity;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.CommentFixture.COMMENT_WRITER_WOOGI;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 댓글(Comment) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스
 */
@DisplayName("[Comment] 단위 테스트")
class CommentTest {

    @DisplayName("정적팩토리 메서드를 이용하여 댓글을 생성한다")
    @Test
    void 댓글_생성() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_WOOGI.createMember();
        Board VOTE_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
        String content = "1번 잠옷 농협은행!";

        // when
        Comment comment = Comment.of(NORMAL_MEMBER, VOTE_BOARD, content);

        // then
        assertThat(comment.getMember()).isEqualTo(NORMAL_MEMBER);
        assertThat(comment.getBoard()).isEqualTo(VOTE_BOARD);
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @DisplayName("등록된 댓글 내용은 다시 수정할 수 있다.")
    @Test
    void 댓글_수정() {
        // given
        Comment TARGET_COMMENT = COMMENT_WRITER_WOOGI.createComment();
        String MODIFY_CONTENT = "2번 잠옷 더 기업은행!";

        // when
        TARGET_COMMENT.changeComment(MODIFY_CONTENT);

        // then
        assertThat(TARGET_COMMENT.getContent()).isEqualTo(MODIFY_CONTENT);
    }

}