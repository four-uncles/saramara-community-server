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
import com.kakao.saramaracommunity.comment.dto.api.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
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

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_게시글에_댓글_작성_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            private Member MEMBER_WOOGI;

            @BeforeEach
            void setUp() {
                MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
            }

            @DisplayName("[Green] 내용을 얼마든지 작성할 수 있다.")
            @Transactional
            @Test
            void 내용을_얼마든지_작성할_수_있다() {
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

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록되지_않은_회원이라면 {

            @DisplayName("[Exception] 내용을 작성할 수 없다.")
            @Test
            void 내용을_작성할_수_없다() {
                // given
                CommentCreateRequest request = new CommentCreateRequest(
                        123L,
                        REGISTED_BOARD.getId(),
                        "불법 광고 댓글"
                );

                // when & then
                assertThatThrownBy(() -> commentService.createComment(request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("존재하지 않는 사용자입니다.");
            }

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_게시글에_댓글_조회_시 {

        private Comment COMMENT_WOOGI;
        private Comment COMMENT_SONNY;

        @BeforeEach
        void setUp() {
            Member MEMBER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
            Member MEMBER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());

            COMMENT_WOOGI = createComment(MEMBER_WOOGI, REGISTED_BOARD);
            COMMENT_SONNY = createComment(MEMBER_SONNY, REGISTED_BOARD);
        }

        @DisplayName("[Green] 누구나 작성자의 닉네임, 내용, 생성시간을 조회할 수 있다.")
        @Test
        void 누구나_등록된_작성자의_닉네임_내용_생성시간을_조회할_수_있다() {
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

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_게시글에_댓글_수정_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            private Member COMMENT_WRITER_WOOGI;
            private Member COMMENT_READER_SONNY;
            private Comment REGISTED_COMMENT;

            @BeforeEach
            void setUp() {
                COMMENT_WRITER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                COMMENT_READER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());
                REGISTED_COMMENT = createComment(COMMENT_WRITER_WOOGI, REGISTED_BOARD);
            }

            @DisplayName("[Green] 자신이 작성한 댓글의 내용을 수정할 수 있다.")
            @Test
            void 자신이_작성한_댓글의_내용을_수정할_수_있다() {
                // given
                Long commentId = REGISTED_COMMENT.getId();
                Long writer = COMMENT_WRITER_WOOGI.getId();
                String updateContent = "2번 잠옷이 귀여워!!";

                CommentUpdateRequest request = new CommentUpdateRequest(writer, updateContent);

                // when
                commentService.updateComment(commentId, request.toServiceRequest());
                Comment result = commentRepository.findById(commentId).orElseThrow();

                // then
                assertThat(result).isNotNull();
                assertThat(result.getContent()).isNotEqualTo("1번 잠옷이 귀여워!!");
                assertThat(result.getContent()).isEqualTo("2번 잠옷이 귀여워!!");
            }

            @DisplayName("[Exception] 자신이 작성하지 않은 댓글의 내용을 수정할 수 없다.")
            @Test
            void 자신이_작성하지_않은_댓글의_내용을_수정할_수_없다() {
                // given
                Long commentId = REGISTED_COMMENT.getId();
                Long reader = COMMENT_READER_SONNY.getId();
                String updateContent = "2번 잠옷이 귀여워!!";

                CommentUpdateRequest request = new CommentUpdateRequest(reader, updateContent);

                // when & then
                assertThatThrownBy(() -> commentService.updateComment(commentId, request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("권한이 없는 사용자입니다.");
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록되지_않은_회원이라면 {

            private Comment REGISTED_COMMENT;

            @BeforeEach
            void setUp() {
                Member COMMENT_WRITER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
                REGISTED_COMMENT = createComment(COMMENT_WRITER_WOOGI, REGISTED_BOARD);
            }

            @DisplayName("[Exception] 수정할 수 없다.")
            @Test
            void 수정할_수_없다() {
                // given
                Long commentId = REGISTED_COMMENT.getId();
                String updateContent = "2번 잠옷이 귀여워!!";

                CommentUpdateRequest request = new CommentUpdateRequest(null, updateContent);

                // when & then
                assertThatThrownBy(() -> commentService.updateComment(commentId, request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("존재하지 않는 사용자입니다.");
            }

        }

    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 등록된_게시글에_댓글_삭제_시 {

        private Member WRITER_WOOGI;
        private Comment REGISTED_COMMENT;

        @BeforeEach
        void setUp() {
            WRITER_WOOGI = createMember(NORMAL_MEMBER_WOOGI.createMember());
            REGISTED_COMMENT = createComment(WRITER_WOOGI, REGISTED_BOARD);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록된_회원이라면 {

            @DisplayName("[Green] 자신이 작성한 댓글은 삭제할 수 있다.")
            @Test
            void 자신의_댓글을_삭제할_수_있다() {
                // given
                Long COMMENT_WRITER = WRITER_WOOGI.getId();
                CommentDeleteRequest request = new CommentDeleteRequest(COMMENT_WRITER);

                // when & then
                assertThatCode(() -> commentService.deleteComment(REGISTED_COMMENT.getId(), request.toServiceRequest()))
                        .doesNotThrowAnyException();
                assertThat(commentRepository.findById(REGISTED_COMMENT.getId()))
                        .isEmpty();
            }

            @DisplayName("[Exception] 자신이 작성하지 않은 댓글은 삭제할 수 없다.")
            @Test
            void 자신이_작성하지_않은_댓글은_삭제할_수_없다() {
                // given
                Member COMMENT_READER_SONNY = createMember(NORMAL_MEMBER_SONNY.createMember());
                CommentDeleteRequest request = new CommentDeleteRequest(COMMENT_READER_SONNY.getId());

                // when & then
                assertThatThrownBy(() -> commentService.deleteComment(REGISTED_COMMENT.getId(), request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("권한이 없는 사용자입니다.");
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 등록되지_않은_회원이라면 {

            @DisplayName("[Exception] 삭제할 수 없다.")
            @Test
            void 삭제할_수_없다() {
                // given
                CommentDeleteRequest request = new CommentDeleteRequest(null);

                // when & then
                assertThatThrownBy(() -> commentService.deleteComment(REGISTED_COMMENT.getId(), request.toServiceRequest()))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage("존재하지 않는 사용자입니다.");
            }

        }

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