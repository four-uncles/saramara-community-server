package com.kakao.saramaracommunity.comment.service;

import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_SONNY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amazonaws.services.kms.model.NotFoundException;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.List;
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
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

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

    private Board createBoard() {
        Member BOARD_WRITER_LANGO = NORMAL_MEMBER_LANGO.createMember();
        Member boardWriter = memberRepository.save(BOARD_WRITER_LANGO);

        Board board = Board.builder()
                .title("집에서 입을 잠옷 어떤 것이 좋을까요?")
                .content("잠옷 후보 3개 정도를 추려봤는데 골라주세요!")
                .categoryBoard(CategoryBoard.VOTE)
                .member(boardWriter)
                .deadLine(LocalDateTime.now())
                .attachPaths(List.of("image-1", "image-2", "image-3"))
                .build();

        return boardRepository.save(board);
    }

    private Member createCommentWriter() {
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        return memberRepository.save(COMMENT_WRITER_SONNY);
    }

}