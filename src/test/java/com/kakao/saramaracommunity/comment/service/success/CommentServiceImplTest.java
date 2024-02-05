package com.kakao.saramaracommunity.comment.service.success;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.tuple;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.CommentService;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("[Comment] 비즈니스 로직 - 성공 테스트")
@ActiveProfiles("test")
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

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("[Success] 댓글 정보를 입력하면, 댓글을 등록한다.")
    @Transactional
    @Test
    void createCommentTest() {
        // given
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        CommentCreateRequest request = new CommentCreateRequest(
                WRITER_WOOGI.getMemberId(),
                REGISTED_BOARD.getId(),
                "1번 잠옷이 귀여워!!"
        );

        // when
        CommentCreateResponse result = commentService.createComment(request.toServiceRequest());

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("1번 잠옷이 귀여워!!");
    }

    @DisplayName("[Success] 한 게시글의 댓글을 조회한다.")
    @Test
    void readCommentsInBoardTest() {
        // given
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Member WRITER_SONNY = createWriter(NORMAL_MEMBER_SONNY.createMember());
        Comment COMMENT_WOOGI = createComment(WRITER_WOOGI, REGISTED_BOARD);
        Comment COMMENT_SONNY = createComment(WRITER_SONNY, REGISTED_BOARD);

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

    @DisplayName("[Success] 작성자는 등록한 댓글의 내용을 수정할 수 있다.")
    @Test
    void updateCommentTest() {
        // given
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        Comment oldComment = createComment(WRITER_WOOGI, REGISTED_BOARD);
        String updateContent = "2번 잠옷이 귀여워!!";

        CommentUpdateRequest request = new CommentUpdateRequest(
                oldComment.getMember().getMemberId(),
                updateContent
        );

        // when
        commentService.updateComment(oldComment.getId(), request.toServiceRequest());
        Comment result = commentRepository.findById(oldComment.getId()).orElseThrow();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEqualTo("1번 잠옷이 귀여워!!");
        assertThat(result.getContent()).isEqualTo("2번 잠옷이 귀여워!!");
    }

    @DisplayName("[Success] 작성자는 등록한 댓글을 삭제할 수 있다.")
    @Test
    void deleteCommentTest() {
        // given
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        Comment RESISTED_COMMENT = createComment(WRITER_WOOGI, REGISTED_BOARD);

        CommentDeleteRequest request = new CommentDeleteRequest(WRITER_WOOGI.getMemberId());

        // when & then
        assertThatCode(
                () -> commentService.deleteComment(RESISTED_COMMENT.getId(), request.toServiceRequest()))
                .doesNotThrowAnyException();
        assertThat(commentRepository.findById(RESISTED_COMMENT.getId())).isEmpty();
    }

    private Comment createComment(Member writer, Board board) {
        Member COMMENT_WRITER = createWriter(writer);
        Comment comment = Comment.builder()
                .member(COMMENT_WRITER)
                .board(board)
                .content("1번 잠옷이 귀여워!!")
                .build();
        return commentRepository.save(comment);
    }

    private Board createBoard(Member writer) {
        Member boardWriter = createWriter(writer);

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

    private Member createWriter(Member member) {
        return memberRepository.save(member);
    }

}