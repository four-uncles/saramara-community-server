package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;

public interface CommentService {

    Long register(CommentDTO commentDTO);

    default Comment dtoToEntity(CommentDTO commentDTO) {

        Member member = Member.builder()
                .email(commentDTO.getWriterEmail())
                .nickname(commentDTO.getWriterNickname())
                .build();


        Board board = Board.builder()
                .id(commentDTO.getBoardId())
                .build();

        Comment comment = Comment.builder()
                .commentId(commentDTO.getCommentId())
                .board(board)
                .member(member)
                .content(commentDTO.getContent())
                .likes(commentDTO.getLikes())
                .pick(commentDTO.getPick())
                .build();

        return comment;
    }

    default CommentDTO entityToDTO(Comment comment) {

        CommentDTO commentDTO = CommentDTO.builder()
                .commentId(comment.getCommentId())
                .boardId(comment.getBoard().getId())
                .writerEmail(comment.getMember().getEmail())
                .writerNickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .pick(comment.getPick())
                .regDate(comment.getCreatedAt())
                .modDate(comment.getUpdatedAt())
                .build();

        return commentDTO;
    }
}
