package com.kakao.saramaracommunity.vote.service;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteCreateRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteDeleteRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteUpdateRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.dto.business.response.VotesReadInBoardResponse;
import com.kakao.saramaracommunity.vote.entity.Vote;
import com.kakao.saramaracommunity.vote.repository.VoteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VoteServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteService voteService;

    private Board REGISTED_BOARD;

    @BeforeEach
    void setUp() {
        Member BOARD_WRITER = NORMAL_MEMBER_LANGO.createMember();
        REGISTED_BOARD = createBoard(BOARD_WRITER);
    }

    @AfterEach
    void tearDown() {
        voteRepository.deleteAllInBatch();
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_게시글에_투표_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            private Member MEMBER_WOOGI;

            @BeforeEach
            void setUp() {
                Member MEMBER_INFO = NORMAL_MEMBER_WOOGI.createMember();
                MEMBER_WOOGI = createMember(MEMBER_INFO);
            }

            @DisplayName("이미지 하나를 선택하여 투표할 수 있다.")
            @Test
            void 이미지_하나를_선택하여_투표할_수_있다() {
                // given
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                VoteCreateRequest request = new VoteCreateRequest(
                        MEMBER_WOOGI.getId(),
                        REGISTED_BOARD.getId(),
                        SELECT_IMAGE.getId()
                );

                // when
                VoteCreateResponse response = voteService.createVote(request.toServiceRequest());

                // then
                assertThat(response).isNotNull();
                assertThat(response.voter()).isEqualTo(MEMBER_WOOGI.getNickname());
                assertThat(response.boardId()).isEqualTo(REGISTED_BOARD.getId());
                assertThat(response.boardImageId()).isEqualTo(SELECT_IMAGE.getId());
            }
        }

    }

    @Disabled("사용자 권한 검증을 위한 보안성 강화 로직을 개발하고 테스트 예정")
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_투표_조회_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표를_진행한_회원이라면 {

            private Member MEMBER_WOOGI;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
                Vote VOTER_WOOGI = createVote(
                        MEMBER_WOOGI,
                        REGISTED_BOARD,
                        REGISTED_BOARD.getBoardImages().get(0)
                );
            }

            @DisplayName("투표 상태를 조회할 수 있다.")
            @Test
            void 투표_상태를_조회할_수_있다() {
                // when
                VotesReadInBoardResponse response = voteService.readVoteInBoard(
                        REGISTED_BOARD.getId());

                // then
                assertThat(response).isNotNull();
                assertThat(response.boardId()).isEqualTo(REGISTED_BOARD.getId());
                assertThat(response.totalVotes()).isEqualTo(1);
                assertThat(response.voteCounts())
                        .hasSize(REGISTED_BOARD.getBoardImages().size())
                        .containsEntry("image-1", 1L)
                        .containsEntry("image-2", 0L)
                        .containsEntry("image-3", 0L);
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표를_진행하지_않은_회원이라면 {

            private Member MEMBER_SONNY;

            @BeforeEach
            void setUp() {
                MEMBER_SONNY = NORMAL_MEMBER_SONNY.createMember();
            }

            @DisplayName("투표 상태를 조회할 수 없다.")
            @Test
            void 투표_상태를_조회할_수_없다() {
                // given
                Long memberId = MEMBER_SONNY.getId();
                Long boardId = REGISTED_BOARD.getId();

                // when & then
                assertThatThrownBy(() -> voteService.readVoteInBoard(boardId))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("권한이 없는 사용자입니다.");
            }

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_투표_수정_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            private Member MEMBER_WOOGI;
            private Vote REGISTED_VOTE;
            private BoardImage SELECT_IMAGE;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                REGISTED_VOTE = createVote(MEMBER_WOOGI, REGISTED_BOARD, SELECT_IMAGE);
            }

            @DisplayName("[Green] 자신이 투표한 이미지 한장을 수정할 수 있다.")
            @Test
            void 자신이_투표한_이미지_한장을_수정할_수_있다() {
                // given
                Long voteId = REGISTED_VOTE.getId();
                Long VOTER_ID = MEMBER_WOOGI.getId();
                BoardImage RESELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(1);

                VoteUpdateRequest request = new VoteUpdateRequest(VOTER_ID, RESELECT_IMAGE);

                // when
                voteService.updateVote(voteId, request.toServiceRequest());
                Vote result = voteRepository.findById(voteId).orElseThrow();

                // then
                assertThat(result).isNotNull();
                assertThat(result.getBoardImage().getPath()).isNotEqualTo("image-1");
                assertThat(result.getBoardImage().getPath()).isEqualTo("image-2");
            }

        }

    }

    @Nested
    @DisplayName("게시글 이미지의 투표 삭제 시")
    class 등록된_투표_삭제_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            private Member MEMBER_WOOGI;
            private Vote REGISTED_VOTE;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                REGISTED_VOTE = createVote(MEMBER_WOOGI, REGISTED_BOARD, SELECT_IMAGE);
            }

            @DisplayName("[Green] 자신의 투표는 삭제할 수 있다.")
            @Test
            void 자신의_투표는_삭제할_수_있다() {
                // given
                Long memberId = MEMBER_WOOGI.getId();
                Long voteId = REGISTED_VOTE.getId();
                VoteDeleteRequest request = new VoteDeleteRequest(memberId);

                // when
                voteService.deleteVote(voteId, request.toServiceRequest());

                // then
                assertThat(voteRepository.findById(voteId)).isEmpty();
            }

        }

    }

    private Vote createVote(Member member, Board board, BoardImage SELECT_IMAGE) {
        Member IMAGE_VOTER = createMember(member);
        Vote vote = Vote.of(
                IMAGE_VOTER,
                board,
                SELECT_IMAGE
        );
        return voteRepository.save(vote);
    }

    private Board createBoard(Member writer) {
        Member boardWriter = createMember(writer);

        Board board = Board.builder()
                .title("집에서 입을 잠옷 어떤 것이 좋을까요?")
                .content("잠옷 후보 3개 정도를 추려봤는데 골라주세요!")
                .categoryBoard(CategoryBoard.VOTE)
                .member(boardWriter)
                .deadLine(LocalDateTime.now())
                .images(List.of("image-1", "image-2", "image-3"))
                .build();

        return boardRepository.save(board);
    }

    private Member createMember(Member member) {
        return memberRepository.save(member);
    }

}