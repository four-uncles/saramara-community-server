package com.kakao.saramaracommunity.comment.service;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.exception.CommentBusinessException;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
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
import org.springframework.transaction.annotation.Transactional;

@DisplayName("댓글은")
class CommentServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardImageRepository boardImageRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    private Board REGISTED_BOARD;

    @BeforeEach
    void setUp() {
        REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("누구나 조회할 수 있다.")
    @Test
    void 누구나_조회할_수_있다() {
        // given
        Member MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
        Member MEMBER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());
        Comment COMMENT_WOOGI = createComment(MEMBER_WOOGI, REGISTED_BOARD);
        Comment COMMENT_SONNY = createComment(MEMBER_SONNY, REGISTED_BOARD);

        // when
        CommentsReadInBoardResponse result = commentService.readCommentsInBoard(REGISTED_BOARD.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.comments())
                .isNotEmpty()
                .hasSize(2)
                .extracting("nickname", "content", "createdAt")
                .contains(
                        tuple(
                                COMMENT_WOOGI.getMember().getNickname(),
                                COMMENT_WOOGI.getContent(),
                                COMMENT_WOOGI.getCreatedAt()
                        ),
                        tuple(
                                COMMENT_SONNY.getMember().getNickname(),
                                COMMENT_SONNY.getContent(),
                                COMMENT_SONNY.getCreatedAt()
                        )
                );
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_회원이라면 {

        private Member MEMBER_WOOGI;

        @BeforeEach
        void setUp() {
            MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
        }

        @DisplayName("얼마든지 작성할 수 있다.")
        @Transactional
        @Test
        void 얼마든지_작성할_수_있다() {
            // given
            CommentCreateRequest firstRequest = new CommentCreateRequest(
                    MEMBER_WOOGI.getId(),
                    REGISTED_BOARD.getId(),
                    "고민이 많이 되네요!!"
            );
            CommentCreateRequest secondRequest = new CommentCreateRequest(
                    MEMBER_WOOGI.getId(),
                    REGISTED_BOARD.getId(),
                    "1번 잠옷이 귀여워!!"
            );

            // when
            CommentCreateResponse firstComment = commentService.createComment(
                    firstRequest.toServiceRequest());
            CommentCreateResponse secondComment = commentService.createComment(
                    secondRequest.toServiceRequest());

            // then
            assertThat(firstComment).isNotNull();
            assertThat(firstComment.nickname()).isEqualTo(MEMBER_WOOGI.getNickname());
            assertThat(firstComment.content()).isEqualTo("고민이 많이 되네요!!");

            assertThat(secondComment).isNotNull();
            assertThat(secondComment.nickname()).isEqualTo(MEMBER_WOOGI.getNickname());
            assertThat(secondComment.content()).isEqualTo("1번 잠옷이 귀여워!!");
        }

        @DisplayName("자신의 댓글을 수정할 수 있다.")
        @Test
        void 자신의_댓글을_수정할_수_있다() {
            // given
            Comment registeredComment = createComment(MEMBER_WOOGI, REGISTED_BOARD);
            String updateContent = "2번 잠옷이 귀여워!!";

            CommentUpdateRequest request = new CommentUpdateRequest(
                    MEMBER_WOOGI.getId(),
                    updateContent
            );

            // when
            commentService.updateComment(
                    registeredComment.getId(),
                    request.toServiceRequest()
            );
            Comment result = commentRepository.findById(registeredComment.getId()).orElseThrow();

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isNotEqualTo("1번 잠옷이 귀여워!!");
            assertThat(result.getContent()).isEqualTo("2번 잠옷이 귀여워!!");
        }

        @DisplayName("자신의 댓글을 삭제할 수 있다.")
        @Test
        void 자신의_댓글을_삭제할_수_있다() {
            // given
            Comment registeredComment = createComment(MEMBER_WOOGI, REGISTED_BOARD);
            CommentDeleteRequest request = new CommentDeleteRequest(
                    MEMBER_WOOGI.getId()
            );

            // when & then
            assertThatCode(() -> commentService.deleteComment(
                        registeredComment.getId(), request.toServiceRequest()
                    )
            ).doesNotThrowAnyException();
            assertThat(commentRepository.findById(registeredComment.getId())).isEmpty();
        }

        @DisplayName("다른 회원의 댓글은 수정할 수 없다.")
        @Test
        void 다른_회원의_댓글은_수정할_수_없다() {
            // given
            Comment COMMENT_WOOGI = createComment(MEMBER_WOOGI, REGISTED_BOARD);
            Member MEMBER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());

            CommentUpdateRequest request = new CommentUpdateRequest(
                    MEMBER_SONNY.getId(),
                    "2번 잠옷이 귀여워!!"
            );


            // when & then
            assertThatThrownBy(() -> commentService.updateComment(
                        COMMENT_WOOGI.getId(), request.toServiceRequest()
                    )
            ).isInstanceOf(CommentBusinessException.class)
                    .hasMessage("권한이 없는 사용자입니다.");
        }

        @DisplayName("다른 회원의 댓글은 삭제할 수 없다.")
        @Test
        void 다른_회원의_댓글은_삭제할_수_없다() {
            // given
            Comment COMMENT_WOOGI = createComment(MEMBER_WOOGI, REGISTED_BOARD);
            Member MEMBER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());
            CommentDeleteRequest request = new CommentDeleteRequest(MEMBER_SONNY.getId());

            // when & then
            assertThatThrownBy(() -> commentService.deleteComment(
                    COMMENT_WOOGI.getId(), request.toServiceRequest()
                )
            ).isInstanceOf(CommentBusinessException.class)
                    .hasMessage("권한이 없는 사용자입니다.");
        }

    }

    @DisplayName("비회원이라면 작성할 수 없다.")
    @Test
    void 비회원_이라면_작성할_수_없다() {
        // given
        CommentCreateRequest request = new CommentCreateRequest(
                Long.MAX_VALUE,
                REGISTED_BOARD.getId(),
                "불법 광고 댓글"
        );

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request.toServiceRequest()))
                .isInstanceOf(CommentBusinessException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
    }

    private Comment createComment(Member writer, Board board) {
        Member COMMENT_WRITER = createMember(writer);
        Comment comment = Comment.of(
                COMMENT_WRITER,
                board,
                "1번 잠옷이 귀여워!!"
        );
        return commentRepository.save(comment);
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