package com.kakao.saramaracommunity.comment.service;

import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_NOT_FOUND;
import static com.kakao.saramaracommunity.comment.exception.CommentErrorCode.COMMENT_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.dto.business.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.request.CommentDeleteServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.request.CommentUpdateServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.exception.CommentBusinessException;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
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
        log.info("[CommentServiceImpl] 요청에 따라 댓글 생성을 시도합니다.");
        Comment comment = commentRepository.save(
                request.toEntity(
                        getMemberEntity(request.memberId()),
                        getBoardEntity(request.boardId())
                )
        );
        log.info("[CommentServiceImpl] 요청에 따라 댓글을 생성하였습니다.");
        return CommentCreateResponse.of(comment);
    }

    @Override
    public CommentsReadInBoardResponse readCommentsInBoard(Long boardId) {
        List<Comment> comments = commentRepository.getCommentsByBoard(boardId);
        log.info("[CommentServiceImpl] 요청에 따라 댓글을 조회합니다.");
        return CommentsReadInBoardResponse.from(comments);
    }

    @Override
    public void updateComment(Long commentId, CommentUpdateServiceRequest request) {
        log.info("[CommentServiceImpl] 요청에 따라 댓글 수정을 시도합니다.");
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        savedComment.changeComment(request.memberId(), request.content());
        log.info("[CommentServiceImpl] 요청에 따라 댓글을 수정 하였습니다.");
    }

    @Override
    public void deleteComment(Long commentId, CommentDeleteServiceRequest request) {
        log.info("[CommentServiceImpl] 요청에 따라 댓글 삭제를 시도합니다.");
        Comment savedComment = getCommentEntity(commentId);
        verifyWriter(savedComment, request.memberId());
        commentRepository.delete(savedComment);
        log.info("[CommentServiceImpl] 요청에 따라 댓글을 삭제 하였습니다.");
    }

    private void verifyWriter(Comment comment, Long memberId) {
        Member writer = comment.getMember();
        if (memberId == null) {
            throw new MemberBusinessException(MEMBER_NOT_FOUND);
        }
        if (!writer.getId().equals(memberId)) {
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

    private Member getMemberEntity(Long memberId) {
        return Optional.ofNullable(memberId)
                .flatMap(memberRepository::findById)
                .orElseThrow(() -> new MemberBusinessException(MEMBER_NOT_FOUND));
    }

    private Board getBoardEntity(Long boardId) {
        return Optional.ofNullable(boardId)
                .flatMap(boardRepository::findById)
                .orElseThrow(() -> new BoardBusinessException(BOARD_NOT_FOUND));
    }

    private Comment getCommentEntity(Long commentId) {
        return Optional.ofNullable(commentId)
                .flatMap(commentRepository::findById)
                .orElseThrow(() -> new CommentBusinessException(COMMENT_NOT_FOUND));
    }

}
