package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;

public interface CommentService {

	public Long createComment(CommentDTO commentDTO);
}
