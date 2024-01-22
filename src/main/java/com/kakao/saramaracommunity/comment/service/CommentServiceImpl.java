package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.exception.CommentErrorCode;
import com.kakao.saramaracommunity.comment.exception.CommentNotFoundException;
import com.kakao.saramaracommunity.comment.exception.CommentUnauthorizedException;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentDeleteServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    /**
     * 댓글 생성을 위한 메서드입니다.
     *
     * @param request 생성에 필요한 commentDTO 입니다.
     * @return 생성된 comment의 고유 id를 반환해줍니다.
     */
    @Override
    public CommentCreateResponse createComment(CommentCreateServiceRequest request) {
        Comment register = commentRepository.save(
                request.toEntity(
                        getMemberEntity(request.memberId()),
                        getBoardEntity(request.boardId())
                )
        );
        return CommentCreateResponse.of(register);
    }

    /**
     * 특정 보드에 대한 모든 댓글을 가져오는 메서드입니다.
     * Stream객체를 이용하여 Comment타입 리스트를 불러온 데이터들을 반환타입인 CommentListDTO 형식으로 convert 해줍니다.
     *
     * @param boardId 특정 보드에 대한 고유id 파라미터입니다.
     * @return 해당 보드에 대한 모든 댓글들을 반환해줍니다.
     * TODO: board에 한번에 조회?
     */
    @Override
    public CommentsReadInBoardResponse readCommentsInBoard(Long boardId) {
        List<Comment> comments = commentRepository.getCommentsByBoard(boardId);
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 조회합니다.");
        return CommentsReadInBoardResponse.from(comments);
    }

    /**
     * 댓글 수정에 관련된 메서드 입니다.
     */
    @Override
    public void updateComment(Long commentId, CommentUpdateServiceRequest request) {
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 수정합니다.");
        savedComment.changeComment(request.content());
    }

    /**
     * 단일 댓글을 삭제하는 메서드입니다.
     * 댓글이 존재하는지 먼저 확인 이후,
     * 존재한다면 delete를 실행하고 true를, 존재하지 않다면 false를 return 해줍니다.
     * @param commentId
     */
    @Override
    public void deleteComment(Long commentId, CommentDeleteServiceRequest request) {
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 삭제합니다.");
        commentRepository.delete(savedComment);
    }

    private void verifyWriter(Comment comment, Long memberId) {
        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new CommentUnauthorizedException(CommentErrorCode.UNAUTHORIZED_TO_UPDATE_COMMENT);
        }
    }

    // TODO: refactor - NotFoundException -> Comment Exception 구현 후 처리 예정
    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // TODO: refactor - NotFoundException -> Comment Exception 구현 후 처리 예정
    private Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private Comment getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException(CommentErrorCode.COMMENT_NOT_FOUND)
        );
    }

}
