package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;

public interface CommentService {

    default Comment dtoToEntity(CommentDTO commentDTO) {

        Comment comment = Comment.builder()
                .board(Board.builder().boardId(commentDTO.getBoardId()).build())
                .member(Member.builder().memberId(commentDTO.getMemberId()).build())
                .content(commentDTO.getContent())
                .likes(commentDTO.getLikes())
                .pick(commentDTO.getPick())
                .build();

        return comment;
    }

    default CommentDTO entityToDTO(Comment comment) {

        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(comment.getCommentId())
                .boardId(comment.getBoard().getBoardId())
                .memberId(comment.getMember().getMemberId())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .pick(comment.getPick())
                .regDate(comment.getCreatedAt())
                .modDate(comment.getUpdatedAt())
                .build();

        return commentDTO;
    }
}
