package com.kakao.saramaracommunity.comment.service;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amazonaws.services.kms.model.NotFoundException;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.exception.CommentUnauthorizedException;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentDeleteServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("[Comment] 비즈니스 로직")
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
        CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                .memberId(createCommentWriter().getMemberId())
                .boardId(createBoard().getId())
                .content("1번 잠옷이 귀여워!!")
                .build();

        // when
        CommentCreateResponse result = commentService.createComment(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("1번 잠옷이 귀여워!!");
    }

    @Disabled("예외 구현 예정 - 비정상적인 방법: 비회원 댓글 등록") // TODO: [Comment] Exception 테스트
    @DisplayName("[Exception] 등록되지 않은 회원은 댓글을 작성할 수 없다.")
    @Test
    void createComment_UnRegister_Member_Exception_Test() {
        // given
        CommentCreateServiceRequest request = CommentCreateServiceRequest.builder()
                .boardId(createBoard().getId())
                .content("1번 잠옷이 귀여워!!")
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("[Success] 작성자는 등록한 댓글의 내용을 수정할 수 있다.")
    @Test
    void updateCommentTest() {
        // given
        Comment oldComment = createComment();
        String updateContent = "2번 잠옷이 귀여워!!";

        CommentUpdateServiceRequest request = CommentUpdateServiceRequest.builder()
                .memberId(oldComment.getMember().getMemberId())
                .boardId(oldComment.getBoard().getId())
                .content(updateContent)
                .build();

        // when
        commentService.updateComment(oldComment.getId(), request);
        Comment result = commentRepository.findById(oldComment.getId()).orElseThrow();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEqualTo("1번 잠옷이 귀여워!!");
        assertThat(result.getContent()).isEqualTo("2번 잠옷이 귀여워!!");
    }

    @DisplayName("[Exception] 작성자의 댓글이 아니면 작성된 댓글을 수정할 수 없다.")
    @Test
    void updateComment_Member_Exception_Test() {
        // given
        Comment oldComment = createComment();
        Member otherMember = createMember();
        String updateContent = "2번 잠옷이 귀여워!!";

        CommentUpdateServiceRequest request = CommentUpdateServiceRequest.builder()
                .memberId(otherMember.getMemberId())
                .boardId(oldComment.getBoard().getId())
                .content(updateContent)
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(oldComment.getId(), request))
                .isInstanceOf(CommentUnauthorizedException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @DisplayName("[Success] 작성자는 등록한 댓글을 삭제할 수 있다.")
    @Test
    void deleteCommentTest() {
        // given
        Comment comment = createComment();
        Member commentWriter = comment.getMember();
        CommentDeleteServiceRequest request = CommentDeleteServiceRequest.builder()
                .memberId(commentWriter.getMemberId())
                .build();

        // when & then
        assertThatCode(() -> commentService.deleteComment(comment.getId(), request))
                .doesNotThrowAnyException();
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @DisplayName("[Exception] 작성자의 댓글이 아니면 작성된 댓글을 삭제할 수 없다.")
    @Test
    void deleteComment_Member_Exception_Test() {
        // given
        Comment comment = createComment();
        Member otherMember = createMember();
        CommentDeleteServiceRequest request = CommentDeleteServiceRequest.builder()
                .memberId(otherMember.getMemberId())
                .build();

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), request))
                .isInstanceOf(CommentUnauthorizedException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    private Comment createComment() {
        Member COMMENT_WRITER = memberRepository.findById(createCommentWriter().getMemberId())
                .orElseThrow();
        Board BOARD_OBJECT = boardRepository.findById(createBoard().getId())
                .orElseThrow();
        Comment comment = Comment.builder()
                .member(COMMENT_WRITER)
                .board(BOARD_OBJECT)
                .content("1번 잠옷이 귀여워!!")
                .build();
        return commentRepository.save(comment);
    }

    private Board createBoard() {
        Member BOARD_WRITER_LANGO = NORMAL_MEMBER_LANGO.createMember();
        Member boardWriter = memberRepository.save(BOARD_WRITER_LANGO);

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

    private Member createCommentWriter() {
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        return memberRepository.save(COMMENT_WRITER_SONNY);
    }

    private Member createMember() {
        Member member = NORMAL_MEMBER_WOOGI.createMember();
        return memberRepository.save(member);
    }

}