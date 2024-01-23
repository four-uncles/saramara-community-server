package com.kakao.saramaracommunity.comment.service.exception;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.exception.CommentUnauthorizedException;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.CommentService;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Comment] 비즈니스 로직 - 예외 테스트")
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

    @DisplayName("[Exception] 회원 정보가 Null 이면 댓글을 작성할 수 없다.")
    @Test
    void createComment_UnRegister_Member_Exception_Null_Test() {
        // given
        Member UNREGISTER_GUEST = createGuest();
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        String content = "불법 광고 댓글";

        // when & then
        assertThatThrownBy(() -> new CommentCreateRequest(UNREGISTER_GUEST.getMemberId(),
                REGISTED_BOARD.getId(),
                content))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("[Exception] 등록되지 않은 회원은 댓글을 작성할 수 없다.")
    @Test
    void createComment_UnRegister_Member_NotFound_Exception_Test() {
        // given
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        String content = "불법 광고 댓글";


        CommentCreateRequest request = new CommentCreateRequest(
                Long.MAX_VALUE,
                REGISTED_BOARD.getId(),
                content
        );

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request.toServiceRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @DisplayName("[Exception] 작성자의 댓글이 아니면 작성된 댓글을 수정할 수 없다.")
    @Test
    void updateComment_Member_Exception_Test() {
        // given
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        Comment REGISTED_COMMENT = createComment(WRITER_WOOGI, REGISTED_BOARD);
        Member WRITER_SONNY = createWriter(NORMAL_MEMBER_SONNY.createMember());
        String updateContent = "2번 잠옷이 귀여워!!";

        CommentUpdateRequest request = new CommentUpdateRequest(
                WRITER_SONNY.getMemberId(),
                updateContent
        );

        // when & then
        assertThatThrownBy(
                () -> commentService.updateComment(REGISTED_COMMENT.getId(), request.toServiceRequest()))
                .isInstanceOf(CommentUnauthorizedException.class)
                .hasMessage("권한이 없는 사용자입니다.");
    }

    @DisplayName("[Exception] 작성자의 댓글이 아니면 작성된 댓글을 삭제할 수 없다.")
    @Test
    void deleteComment_Member_Exception_Test() {
        // given
        Member WRITER_WOOGI = createWriter(NORMAL_MEMBER_WOOGI.createMember());
        Board REGISTED_BOARD = createBoard(NORMAL_MEMBER_LANGO.createMember());
        Comment REGISTED_COMMENT = createComment(WRITER_WOOGI, REGISTED_BOARD);
        Member WRITER_SONNY = createWriter(NORMAL_MEMBER_SONNY.createMember());
        CommentDeleteRequest request = new CommentDeleteRequest(WRITER_SONNY.getMemberId());

        // when & then
        assertThatThrownBy(
                () -> commentService.deleteComment(REGISTED_COMMENT.getId(), request.toServiceRequest()))
                .isInstanceOf(CommentUnauthorizedException.class)
                .hasMessage("권한이 없는 사용자입니다.");
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

    private Member createGuest() {
        return new Member(null, null, null);
    }

}