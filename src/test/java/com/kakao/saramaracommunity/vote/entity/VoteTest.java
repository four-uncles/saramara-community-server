package com.kakao.saramaracommunity.vote.entity;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 투표(Vote) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스
 */
class VoteTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 투표_생성_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 회원_사용자_정보와_게시글_정보가_주어진다면 {

            private Member MEMBER_WOOGI;
            private Board REGISTED_BOARD;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
                REGISTED_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            }

            @Test
            @DisplayName("여러 이미지 중에서 하나의 이미지 항목을 선택하여 투표 객체로 생성할 수 있다.")
            void 여러_이미지_중에서_하나의_이미지_항목을_선택하여_투표_객체로_생성할_수_있다() {
                // given
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);

                // when
                Vote vote = Vote.of(MEMBER_WOOGI, REGISTED_BOARD, SELECT_IMAGE);

                // then
                assertThat(vote.getMember()).isEqualTo(MEMBER_WOOGI);
                assertThat(vote.getBoard()).isEqualTo(REGISTED_BOARD);
                assertThat(vote.getBoardImage()).isEqualTo(SELECT_IMAGE);
            }

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 투표_수정_시 {

        private static Board REFISTED_VOTE_BOARD;
        private static Vote REGISTED_VOTE; // REFISTED_VOTE_BOARD의 투표
        private static Member MEMBER_WOOGI; // REGISTED_VOTE의 투표자
        private static Member MEMBER_SONNY;


        @BeforeEach
        void setUp() {
            MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
            setField(MEMBER_WOOGI, "id", 1L);

            MEMBER_SONNY = NORMAL_MEMBER_SONNY.createMember();
            setField(MEMBER_SONNY, "id", 2L);

            REFISTED_VOTE_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            setField(REFISTED_VOTE_BOARD, "id", 1L);
            setField(REFISTED_VOTE_BOARD.getMember(), "id", 3L);
            setField(REFISTED_VOTE_BOARD.getBoardImages().get(0), "id", 1L);
            setField(REFISTED_VOTE_BOARD.getBoardImages().get(1), "id", 2L);
            setField(REFISTED_VOTE_BOARD.getBoardImages().get(2), "id", 3L);

            BoardImage SELECT_IMAGE = REFISTED_VOTE_BOARD.getBoardImages().get(0);
            REGISTED_VOTE = createVote(MEMBER_WOOGI, REFISTED_VOTE_BOARD, SELECT_IMAGE);

            setField(REGISTED_VOTE, "id", 1L);
        }

        @Test
        @DisplayName("투표자 정보가 일치한다면 투표 객체를 수정할 수 있다.")
        void 투표자_정보가_일치한다면_투표_객체를_수정할_수_있다() {
            // given
            Member REQUEST_MEMBER = MEMBER_WOOGI;
            BoardImage UPDATE_IMAGE = REFISTED_VOTE_BOARD.getBoardImages().get(1);

            // when
            REGISTED_VOTE.changeVote(REQUEST_MEMBER.getId(), UPDATE_IMAGE);

            // then
            assertThat(REGISTED_VOTE.getBoardImage()).isEqualTo(UPDATE_IMAGE);
        }

        @Test
        @DisplayName("투표자 정보가 일치하지 않는다면 투표 객체를 수정할 수는 없다.")
        void 투표자_정보가_일치하지_않는다면_투표_객체를_수정할_수는_없다() {
            // given
            Member REQUEST_MEMBER = MEMBER_SONNY;
            BoardImage UPDATE_IMAGE = REFISTED_VOTE_BOARD.getBoardImages().get(1);

            // when & then
            assertThatThrownBy(() -> REGISTED_VOTE.changeVote(REQUEST_MEMBER.getId(), UPDATE_IMAGE))
                    .isInstanceOf(MemberBusinessException.class)
                    .hasMessage("권한이 없는 사용자입니다.");
        }

        private static Vote createVote(
                Member NORMAL_MEMBER,
                Board VOTE_BOARD,
                BoardImage SELECT_IMAGE
        ) {

            return Vote.of(NORMAL_MEMBER, VOTE_BOARD, SELECT_IMAGE);
        }

    }

}
