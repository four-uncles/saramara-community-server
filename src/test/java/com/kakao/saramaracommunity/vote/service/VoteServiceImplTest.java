package com.kakao.saramaracommunity.vote.service;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteCreateRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.repository.VoteRepository;
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
    class 등록된_게시글에_이미지_투표_시 {

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