package com.kakao.saramaracommunity.comment.entity;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.CommentFixture.COMMENT_WRITER_WOOGI;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 댓글(Comment) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스
 */
class CommentTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 댓글_작성_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 회원_정보와_게시글_정보가_주어지는_경우 {

            private Member MEMBER_WOOGI;
            private Board REGISTED_BOARD;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
                REGISTED_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            }

            @DisplayName("올바른 객체를 생성할 수 있다.")
            @Test
            void 객체를_생성할_수_있다() {
                // given
                String content = "1번 잠옷 농협은행!";

                // when
                Comment comment = Comment.of(MEMBER_WOOGI, REGISTED_BOARD, content);

                // then
                assertThat(comment.getMember()).isEqualTo(MEMBER_WOOGI);
                assertThat(comment.getBoard()).isEqualTo(REGISTED_BOARD);
                assertThat(comment.getContent()).isEqualTo(content);
            }

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 댓글_수정_시 {

        private static Comment REGISTED_COMMENT;

        @BeforeEach
        void setUp() {
            REGISTED_COMMENT = COMMENT_WRITER_WOOGI.createComment();
            setField(REGISTED_COMMENT, "id", 1L);
            setField(COMMENT_WRITER_WOOGI.getWriter(), "id", 1L);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 회원_정보와_댓글의_작성자가_일치하는_경우 {

            @DisplayName("수정할 수 있다.")
            @Test
            void 수정할_수_있다() {
                // given
                Member COMMENT_WRITER = COMMENT_WRITER_WOOGI.getWriter();
                String MODIFY_CONTENT = "2번 잠옷 더 기업은행!";

                // when
                REGISTED_COMMENT.changeComment(COMMENT_WRITER.getId(), MODIFY_CONTENT);

                // then
                assertThat(REGISTED_COMMENT.getContent()).isEqualTo(MODIFY_CONTENT);
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 회원_정보와_댓글의_작성자가_일치하지_않는_경우 {

            private Member MEMBER_SONNY;

            @BeforeEach
            void setUp() {
                MEMBER_SONNY = NORMAL_MEMBER_SONNY.createMember();
                setField(MEMBER_SONNY, "id", 2L);
            }

            @DisplayName("수정할 수 없다.")
            @Test
            void 수정할_수_없다() {
                // given
                String MODIFY_CONTENT = "2번 잠옷 더 기업은행!";

                // when & then
                assertThatThrownBy(
                        () -> REGISTED_COMMENT.changeComment(MEMBER_SONNY.getId(), MODIFY_CONTENT))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("권한이 없는 사용자입니다.");
            }

        }

    }

}