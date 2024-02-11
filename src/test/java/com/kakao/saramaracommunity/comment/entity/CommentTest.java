package com.kakao.saramaracommunity.comment.entity;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.CommentFixture.COMMENT_WRITER_WOOGI;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.exception.CommentBusinessException;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 댓글(Comment) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스
 */
@DisplayName("댓글은")
class CommentTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_정보와_게시글_정보가_존재하는_경우 {

        @DisplayName("생성할 수 있다.")
        @Test
        void 생성할_수_있다() {
            // given
            Board REGISTED_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            Member MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
            String content = "1번 잠옷 농협은행!";

            setField(REGISTED_BOARD, "id", 1L);
            setField(MEMBER_WOOGI, "id", 2L);

            // when
            Comment comment = Comment.of(MEMBER_WOOGI, REGISTED_BOARD, content);

            // then
            assertThat(comment.getMember()).isEqualTo(MEMBER_WOOGI);
            assertThat(comment.getBoard()).isEqualTo(REGISTED_BOARD);
            assertThat(comment.getContent()).isEqualTo(content);
        }

        @DisplayName("수정할 수 있다.")
        @Test
        void 수정할_수_있다() {
            // given
            Comment REGISTED_COMMENT = COMMENT_WRITER_WOOGI.createComment();
            Member MEMBER_WOOGI = COMMENT_WRITER_WOOGI.getWriter();
            String MODIFY_CONTENT = "2번 잠옷 더 기업은행!";

            // when
            REGISTED_COMMENT.changeComment(MEMBER_WOOGI.getId(), MODIFY_CONTENT);

            // then
            assertThat(REGISTED_COMMENT.getContent()).isEqualTo(MODIFY_CONTENT);
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 회원_정보가_존재하지_않으면 {

        @DisplayName("생성할 수 없고, MEMBER_NOT_FOUND 예외가 발생한다.")
        @Test
        void 생성할_수_없다() {
            // given
            Board REGISTED_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            String content = "불법 광고 댓글";

            setField(REGISTED_BOARD, "id", 1L);

            // when & then
            assertThatThrownBy(() -> Comment.of(null, REGISTED_BOARD, content))
                    .isInstanceOf(CommentBusinessException.class);
        }

        @DisplayName("수정할 수 없고, MEMBER_NOT_FOUND 예외가 발생한다.")
        @Test
        void 수정할_수_없다() {
            // given
            Comment REGISTED_COMMENT = COMMENT_WRITER_WOOGI.createComment();
            String MODIFY_CONTENT = "2번 잠옷 더 기업은행!";

            // when & then
            assertThatThrownBy(() -> REGISTED_COMMENT.changeComment(
                    null, MODIFY_CONTENT
                )
            ).isInstanceOf(CommentBusinessException.class);
        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 게시글_정보가_존재하지_않으면 {

        @DisplayName("생성할 수 없고, BOARD_NOT_FOUND 예외가 발생한다.")
        @Test
        void 생성할_수_없다() {
            // given
            Member MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
            String content = "1번 잠옷 귀여워!!";

            setField(MEMBER_WOOGI, "id", 2L);

            // when & then
            assertThatThrownBy(() -> Comment.of(MEMBER_WOOGI, null, content))
                    .isInstanceOf(CommentBusinessException.class);
        }

    }

}