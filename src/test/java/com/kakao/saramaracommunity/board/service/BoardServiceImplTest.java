package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardDeleteServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardUpdateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.CHOICE;
import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT;
import static com.kakao.saramaracommunity.fixture.MemberFixture.*;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * 게시글(Board) 관련 비즈니스 계층의 통합 테스트를 진행하는 테스트 클래스입니다.
 */
class BoardServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Autowired
    private BoardService boardService;

    @AfterEach
    void tearDown() {
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    private Member MEMBER_LANGO;
    private Member MEMBER_SONNY;

    @BeforeEach
    void setUp() {
        MEMBER_LANGO = memberRepository.save(NORMAL_MEMBER_LANGO.createMember());
        MEMBER_SONNY = memberRepository.save(NORMAL_MEMBER_SONNY.createMember());
    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 게시글_조회_시 {
        private Board SAVED_BOARD;
        @BeforeEach
        void setUp() {
            SAVED_BOARD = boardRepository.save(createBoard(CHOICE, MEMBER_LANGO, createImagePaths(1)));
        }
        @Test
        @DisplayName("[Green] 누구나 상세 정보를 조회할 수 있다.")
        void 누구나_상세_정보를_조회할_수_있다() {
            // when
            BoardGetResponse result = boardService.getBoard(SAVED_BOARD.getId());

            // then
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.content()).isEqualTo("content");
            assertThat(result.categoryBoard()).isEqualTo(CHOICE);
            assertThat(result.boardImages()).hasSize(1);
        }
        // searchBoard 메서드는 아직 미완성입니다.
        @Disabled
        @Test
        @DisplayName("[Green] 누구나 모든 페이지 목록을 조회할 수 있다.")
        void 누구나_모든_페이지_목록을_조회할_수_있다() {
            // given
            // when
            // then
        }
    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 게시글_작성_시 {
        @Nested
        @DisplayNameGeneration(ReplaceUnderscores.class)
        class 등록된_회원이라면 {
            @Test
            @DisplayName("[Green] 투표 카테고리의 글을 작성할 때 최소 2장 이상의 이미지를 등록해야 한다.")
            void 투표_카테고리의_글을_작성할_때_최소_2장_이상의_이미지를_등록해야_한다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(VOTE, MEMBER_LANGO, createImagePaths(2));

                // when
                BoardCreateResponse result = boardService.createBoard(request);

                // then
                assertThat(result.categoryBoard()).isEqualTo(VOTE);
                assertThat(result.boardImages()).hasSize(2);
            }
            @Test
            @DisplayName("[Green] 투표 카테고리의 글을 작성할 때 최대 5장까지 이미지를 등록할 수 있다.")
            void 투표_카테고리의_글을_작성할_때_최대_5장까지_이미지를_등록할_수_있다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(VOTE, MEMBER_LANGO, createImagePaths(5));

                // when
                BoardCreateResponse result = boardService.createBoard(request);

                // then
                assertThat(result.categoryBoard()).isEqualTo(VOTE);
                assertThat(result.boardImages()).hasSize(5);
            }
            @Test
            @DisplayName("[Edge] 투표 카테고리의 글을 작성할 때 이미지는 1장만 등록할 수는 없다.")
            void 투표_카테고리의_글을_작성할_때_이미지는_1장만_등록할_수는_없다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(VOTE, MEMBER_LANGO, createImagePaths(1));

                // when & then
                assertThatThrownBy(() -> boardService.createBoard(request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
            @Test
            @DisplayName("[Edge] 투표 카테고리의 글을 작성할 때 이미지는 최대 5장을 초과할 수 없다.")
            void 투표_카테고리의_글을_작성할_때_이미지는_최대_5장을_초과할_수_없다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(VOTE, MEMBER_LANGO, createImagePaths(6));

                // when & then
                assertThatThrownBy(() -> boardService.createBoard(request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
            @Test
            @DisplayName("[Green] 찬반 카테고리의 글을 작성할 때 이미지는 1장만 등록할 수 있다.")
            void 찬반_카테고리의_글을_작성할_때_이미지는_1장만_등록할_수_있다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(CHOICE, MEMBER_LANGO, createImagePaths(1));

                // when
                BoardCreateResponse result = boardService.createBoard(request);

                // then
                assertThat(result.categoryBoard()).isEqualTo(CHOICE);
                assertThat(result.boardImages()).hasSize(1);
            }
            @Test
            @DisplayName("[Edge] 찬반 카테고리의 글을 작성할 때 이미지는 2장 이상 등록할 수 없다.")
            void 찬반_카테고리의_글을_작성할_때_이미지는_2장_이상_등록할_수_없다() {
                // given
                BoardCreateServiceRequest request = createSaveRequest(CHOICE, MEMBER_LANGO, createImagePaths(2));

                // when & then
                assertThatThrownBy(() -> boardService.createBoard(request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
            }
        }
        @Nested
        @DisplayNameGeneration(ReplaceUnderscores.class)
        class 등록되지_않은_회원이라면 {
            @Test
            @DisplayName("[Red] 작성할 수 없다.")
            void 작성할_수_없다() {
                // given
                Member NOT_SAVED_MEMBER = NORMAL_MEMBER_WOOGI.createMember();
                BoardCreateServiceRequest request = createSaveRequest(VOTE, NOT_SAVED_MEMBER, createImagePaths(2));

                // when & then
                assertThatThrownBy(() -> boardService.createBoard(request))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage(MEMBER_NOT_FOUND.getMessage());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 게시글_수정_시 {
        private Board SAVED_VOTE_BOARD;
        private Board SAVED_CHOICE_BOARD;
        @BeforeEach
        void setUp() {
            SAVED_VOTE_BOARD = createBoard(VOTE, MEMBER_LANGO, createImagePaths(3));
            SAVED_CHOICE_BOARD = createBoard(CHOICE, MEMBER_LANGO, createImagePaths(1));
            boardRepository.saveAll(List.of(SAVED_VOTE_BOARD, SAVED_CHOICE_BOARD));
        }
        @Nested
        @DisplayNameGeneration(ReplaceUnderscores.class)
        class 등록된_회원이라면 {
            @Test
            @DisplayName("[Green] 본인의 게시글을 수정할 수 있다.")
            void 본인의_게시글을_수정할_수_있다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_LANGO, createImagePaths(4));

                // when
                boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request);

                // then
                Optional<Board> result = boardRepository.findById(SAVED_VOTE_BOARD.getId());
                assertThat(result.get().getTitle()).isEqualTo("update-title");
                assertThat(result.get().getContent()).isEqualTo("update-content");
            }
            @Test
            @DisplayName("[Red] 다른 회원의 게시글은 수정할 수 없다.")
            void 다른_회원의_게시글은_수정할_수_없다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_SONNY, createImagePaths(4));

                // when & then
                assertThatThrownBy(() -> boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage(UNAUTHORIZED_TO_MEMBER.getMessage());
            }
            @Test
            @DisplayName("[Green] 투표 카테고리의 글을 수정할 때 이미지는 2장까지 삭제할 수 있다.")
            void 투표_카테고리의_글을_수정할_때_이미지는_2장까지_삭제할_수_있다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_LANGO, createImagePaths(2));

                // when
                boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request);

                // then
                List<BoardImage> result = boardImageRepository.findByBoardId(SAVED_VOTE_BOARD.getId());
                assertThat(result).hasSize(2);
            }
            @Test
            @DisplayName("[Green] 투표 카테고리의 글을 수정할 때 이미지는 최대 5장까지 추가로 등록할 수 있다.")
            void 투표_카테고리의_글을_수정할_때_이미지는_최대_5장까지_추가로_등록할_수_있다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_LANGO, createImagePaths(5));

                // when
                boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request);

                // then
                List<BoardImage> result = boardImageRepository.findByBoardId(SAVED_VOTE_BOARD.getId());
                assertThat(result).hasSize(5);
            }
            @Test
            @DisplayName("[Edge] 투표 카테고리의 글을 수정할 때 이미지는 최소 2장 미만으로 삭제할 수 없다.")
            void 투표_카테고리의_글을_수정할_때_이미지는_최소_2장_미만으로_삭제할_수_없다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_LANGO, createImagePaths(1));

                // when & then
                assertThatThrownBy(() -> boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
            @Test
            @DisplayName("[Edge] 투표 카테고리의 글을 수정할 때 이미지는 5장을 초과할 수 없다.")
            void 투표_카테고리의_글을_수정할_때_이미지는_5장을_초과할_수_없다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, MEMBER_LANGO, createImagePaths(6));

                // when & then
                assertThatThrownBy(() -> boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
            @Test
            @DisplayName("[Green] 찬반 카테고리의 글을 수정할 때 이미지는 1장만 수정해야 한다.")
            void 찬반_카테고리의_글을_수정할_때_이미지는_1장만_수정해야_한다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(CHOICE, MEMBER_LANGO, createImagePaths(1));

                // when
                boardService.updateBoard(SAVED_CHOICE_BOARD.getId(), request);

                // then
                List<BoardImage> result = boardImageRepository.findByBoardId(SAVED_CHOICE_BOARD.getId());
                assertThat(result).hasSize(1);
            }
            @Test
            @DisplayName("[Edge] 찬반 카테고리의 글을 수정할 때 이미지는 2장 이상 추가할 수 없다.")
            void 찬반_카테고리의_글을_수정할_때_이미지는_2장_이상_추가할_수_없다() {
                // given
                BoardUpdateServiceRequest request = createUpdateRequest(CHOICE, MEMBER_LANGO, createImagePaths(2));

                // when & then
                assertThatThrownBy(() -> boardService.updateBoard(SAVED_CHOICE_BOARD.getId(), request))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
            }
        }
        @Nested
        @DisplayNameGeneration(ReplaceUnderscores.class)
        class 등록되지_않은_회원이라면 {
            @Test
            @DisplayName("[Red] 수정할 수 없다.")
            void 수정할_수_없다() {
                // given
                Member NOT_SAVED_MEMBER = NORMAL_MEMBER_WOOGI.createMember();
                BoardUpdateServiceRequest request = createUpdateRequest(VOTE, NOT_SAVED_MEMBER, createImagePaths(3));

                // when & then
                assertThatThrownBy(() -> boardService.updateBoard(SAVED_VOTE_BOARD.getId(), request))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage(UNAUTHORIZED_TO_MEMBER.getMessage());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 게시글_삭제_시 {
        private Board SAVED_BOARD;
        @BeforeEach
        void setUp() {
            SAVED_BOARD = boardRepository.save(createBoard(VOTE, MEMBER_LANGO, createImagePaths(3)));
        }
        @Test
        @DisplayName("[Green] 등록된 게시글 이미지도 모두 삭제된다.")
        void 등록된_게시글_이미지도_모두_삭제된다() {
            // given
            BoardDeleteServiceRequest request = createDeleteRequest(MEMBER_LANGO.getId());

            // when
            boardService.deleteBoard(SAVED_BOARD.getId(), request);

            // then
            Optional<Board> result = boardRepository.findById(SAVED_BOARD.getId());
            List<BoardImage> deletedImages = boardImageRepository.findByBoardId(SAVED_BOARD.getId());
            assertThat(result).isEmpty();
            assertThat(deletedImages).isEmpty();
        }
        @Test
        @DisplayName("[Red] 다른 회원의 게시글은 삭제할 수 없다.")
        void 다른_회원의_게시글은_삭제할_수_없다() {
            // given
            BoardDeleteServiceRequest request = createDeleteRequest(MEMBER_SONNY.getId());

            // when & then
            assertThatThrownBy(() -> boardService.deleteBoard(SAVED_BOARD.getId(), request))
                    .isInstanceOf(MemberBusinessException.class)
                    .hasMessage(UNAUTHORIZED_TO_MEMBER.getMessage());

        }
    }

    private static Board createBoard(
            CategoryBoard category,
            Member savedMember,
            List<String> images
    ) {
        return Board.builder()
                .title("title")
                .content("content")
                .member(savedMember)
                .categoryBoard(category)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();
    }

    private static List<String> createImagePaths(
            int size
    ) {
        return IntStream.range(0, size)
                .mapToObj(i -> "s3-image-path-" + (i+1))
                .collect(Collectors.toList());
    }

    private static BoardCreateServiceRequest createSaveRequest(
            CategoryBoard category,
            Member member,
            List<String> images
    ) {
        return BoardCreateServiceRequest.builder()
                .title("title")
                .content("content")
                .categoryBoard(category)
                .memberId(member.getId())
                .deadLine(LocalDateTime.now())
                .boardImages(images)
                .build();
    }

    private static BoardUpdateServiceRequest createUpdateRequest(
            CategoryBoard category,
            Member member,
            List<String> images
    ) {
        return BoardUpdateServiceRequest.builder()
                .memberId(member.getId())
                .title("update-title")
                .content("update-content")
                .categoryBoard(category)
                .deadLine(LocalDateTime.now())
                .boardImages(images)
                .build();
    }

    private static BoardDeleteServiceRequest createDeleteRequest(
            Long memberId
    ) {
        return BoardDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
