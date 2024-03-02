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
import com.kakao.saramaracommunity.vote.exception.VoteBusinessException;
import com.kakao.saramaracommunity.vote.repository.VoteRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @AfterEach
    void tearDown() {
        voteRepository.deleteAllInBatch();
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 특정_게시글에서 {

        private Board REGISTED_BOARD;

        @BeforeEach
        void setUp() {
            Member BOARD_WRITER = NORMAL_MEMBER_LANGO.createMember();
            REGISTED_BOARD = createBoard(BOARD_WRITER);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표를_할_경우 {

            private Member MEMBER_WOOGI; // 투표자

            @BeforeEach
            void setUp() {
                Member MEMBER_INFO = NORMAL_MEMBER_WOOGI.createMember();
                MEMBER_WOOGI = createMember(MEMBER_INFO);
            }

            @Test
            @DisplayName("[Green] 하나의 항목을 선택해 투표할 수 있다.")
            void 하나의_항목을_선택해_투표할_수_있다() {
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

            @Test
            @DisplayName("[Red] 중복 투표는 할 수 없다.")
            void 중복_투표는_할_수_없다() {
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                VoteCreateRequest request = new VoteCreateRequest(
                        MEMBER_WOOGI.getId(),
                        REGISTED_BOARD.getId(),
                        SELECT_IMAGE.getId()
                );

                // when
                voteService.createVote(request.toServiceRequest());

                // then
                assertThatThrownBy(() -> voteService.createVote(request.toServiceRequest()))
                        .isInstanceOf(VoteBusinessException.class)
                        .hasMessage("이미 투표한 상태입니다.");
            }

            @Test
            @DisplayName("[Red] 회원이 아닐 경우 투표할 수 없다.")
            void 회원이_아닐_경우_투표할_수_없다() {
                // given
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                VoteCreateRequest request = new VoteCreateRequest(
                        null,
                        REGISTED_BOARD.getId(),
                        SELECT_IMAGE.getId()
                );
                
                // when & then
                assertThatThrownBy(() -> voteService.createVote(request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("존재하지 않는 사용자입니다.");
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표를_조회할_경우 {

            private Member MEMBER_WOOGI;
            private Member MEMBER_SONNY;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = NORMAL_MEMBER_WOOGI.createMember();
                MEMBER_SONNY = NORMAL_MEMBER_SONNY.createMember();
                createVote(
                        MEMBER_WOOGI,
                        REGISTED_BOARD,
                        REGISTED_BOARD.getBoardImages().get(0)
                );
                createVote(
                        MEMBER_SONNY,
                        REGISTED_BOARD,
                        REGISTED_BOARD.getBoardImages().get(1)
                );
            }

            @Test
            @DisplayName("[Green] 비회원이라도 투표 현황을 알 수 있다.")
            void 비회원이라도_투표_현황을_알_수_있다() {
                // when
                VotesReadInBoardResponse response = voteService.readVoteInBoard(
                        REGISTED_BOARD.getId(), null);

                // then
                assertThat(response).isNotNull();
                assertThat(response.boardId()).isEqualTo(REGISTED_BOARD.getId());
                assertThat(response.isVoted()).isEqualTo(false);
                assertThat(response.totalVotes()).isEqualTo(2);
                assertThat(response.voteCounts())
                        .hasSize(REGISTED_BOARD.getBoardImages().size())
                        .containsEntry("image-1", 1L)
                        .containsEntry("image-2", 1L)
                        .containsEntry("image-3", 0L);
            }

            @Test
            @DisplayName("[Green] 투표를 진행한 경우 투표 완료 상태를 알 수 있다.")
            void 투표를_진행한_경우_투표_완료_상태를_알_수_있다() {
                // given
                Principal mockPrincipal = new Principal() {
                    @Override
                    public String getName() {
                        return "gitshineit@gmail.com"; // 투표 완료 회원
                    }
                };

                // when
                VotesReadInBoardResponse response = voteService.readVoteInBoard(
                        REGISTED_BOARD.getId(), mockPrincipal);

                // then
                assertThat(response.boardId()).isEqualTo(REGISTED_BOARD.getId());
                assertThat(response.isVoted()).isEqualTo(true);
            }

            @Test
            @DisplayName("[Green] 투표를 진행하지 않은 경우 투표 미완료 상태를 알 수 있다.")
            void 투표를_진행하지_않은_경우_투표_미완료_상태를_알_수_있다() {
                // given
                Principal mockPrincipal = new Principal() {
                    @Override
                    public String getName() {
                        return "lango@gmail.com"; // 투표 미완료 회원
                    }
                };

                // when
                VotesReadInBoardResponse response = voteService.readVoteInBoard(
                        REGISTED_BOARD.getId(), mockPrincipal);

                // then
                assertThat(response.boardId()).isEqualTo(REGISTED_BOARD.getId());
                assertThat(response.isVoted()).isEqualTo(false);
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 다시_재투표할_경우 {

            private Member MEMBER_WOOGI;
            private BoardImage SELECT_IMAGE;
            private Long voteId;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                createVote(MEMBER_WOOGI, REGISTED_BOARD, SELECT_IMAGE);
                voteId = voteRepository.findByMemberIdAndBoardId(MEMBER_WOOGI.getId(), REGISTED_BOARD.getId())
                        .map(Vote::getId)
                        .orElse(null);
            }

            @Test
            @DisplayName("[Green] 이미 투표를 했어도 다시 투표할 수 있다.")
            void 이미_투표를_했어도_다시_투표할_수_있다() {
                // given
                BoardImage RESELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(1);
                VoteUpdateRequest request = new VoteUpdateRequest(
                        MEMBER_WOOGI.getId(), RESELECT_IMAGE
                );

                // when
                voteService.updateVote(voteId, request.toServiceRequest());
                Vote result = voteRepository.findById(voteId).orElseThrow();

                // then
                assertThat(result).isNotNull();
                assertThat(result.getBoardImage().getPath()).isNotEqualTo("image-1");
                assertThat(result.getBoardImage().getPath()).isEqualTo("image-2");
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표를_취소할_경우 {

            private Member MEMBER_WOOGI;
            private Long voteId;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                BoardImage SELECT_IMAGE = REGISTED_BOARD.getBoardImages().get(0);
                createVote(MEMBER_WOOGI, REGISTED_BOARD, SELECT_IMAGE);
                voteId = voteRepository.findByMemberIdAndBoardId(MEMBER_WOOGI.getId(), REGISTED_BOARD.getId())
                        .map(Vote::getId)
                        .orElse(null);
            }

            @Test
            @DisplayName("[Green] 정상적으로 투표 내역이 초기화된다.")
            void 정상적으로_투표_내역이_초기화된다() {
                // given
                VoteDeleteRequest request = new VoteDeleteRequest(MEMBER_WOOGI.getId());

                // when
                voteService.deleteVote(voteId, request.toServiceRequest());

                // then
                assertThat(voteRepository.findById(voteId)).isEmpty();
            }

        }

    }

    private void createVote(Member member, Board board, BoardImage SELECT_IMAGE) {
        Member IMAGE_VOTER = createMember(member);
        Vote vote = Vote.of(
                IMAGE_VOTER,
                board,
                SELECT_IMAGE
        );
        voteRepository.save(vote);
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