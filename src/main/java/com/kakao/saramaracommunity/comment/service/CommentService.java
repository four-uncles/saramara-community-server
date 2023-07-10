package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.dto.CommentListDTO;

import java.util.List;

public interface CommentService {

	public Long createComment(CommentDTO commentDTO);

	public List<CommentListDTO> getBoardComments(Long BoardId);
}
