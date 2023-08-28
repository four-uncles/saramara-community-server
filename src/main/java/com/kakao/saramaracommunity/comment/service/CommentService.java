package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.service.dto.response.CommentListDTO;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequset;

import java.util.List;

public interface CommentService {

	Long createComment(CommentCreateServiceRequest request);

	List<CommentListDTO> getBoardComments(Long boardId);

	Boolean updateComment(Long commentId, CommentUpdateServiceRequset requset);

	Boolean deleteComment(Long commentId);

}
