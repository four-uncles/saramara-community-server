package com.kakao.saramaracommunity.vote.entity;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.member.entity.Member;
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
        class 회원_정보와_게시글_정보가_주어지는_경우 {

            private Member MEMBER_WOOGI;
            private Board REGISTED_BOARD;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
                REGISTED_BOARD = BOARD_VOTE_WRITER_LANGO.createBoard();
            }

            @DisplayName("한 이미지를 선택하여 올바른 투표 객체를 생성할 수 있다.")
            @Test
            void 한_이미지를_선택하여_올바른_투표_객체를_생성할_수_있다() {
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

}