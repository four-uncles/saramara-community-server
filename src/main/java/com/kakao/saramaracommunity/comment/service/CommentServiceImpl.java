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

    @Override
    public CommentsReadInBoardResponse readCommentsInBoard(Long boardId) {
        List<Comment> comments = commentRepository.getCommentsByBoard(boardId);
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 조회합니다.");
        return CommentsReadInBoardResponse.from(comments);
    }

    @Override
    public void updateComment(Long commentId, CommentUpdateServiceRequest request) {
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 수정합니다.");
        savedComment.changeComment(request.content());
    }

    @Override
    public void deleteComment(Long commentId, CommentDeleteServiceRequest request) {
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        log.info("[CommentServiceImpl.class] 요청에 따라 댓글을 삭제합니다.");
        commentRepository.delete(savedComment);
    }

    private void verifyWriter(Comment comment, Long memberId) {
        if (!comment.getMember().getId().equals(memberId)) {
            throw new CommentUnauthorizedException(CommentErrorCode.UNAUTHORIZED_TO_UPDATE_COMMENT);
        }
    }

    // TODO: refactor - IllegalArgumentException -> Comment Exception 구현 후 처리 예정
    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // TODO: refactor - IllegalArgumentException -> Comment Exception 구현 후 처리 예정
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
