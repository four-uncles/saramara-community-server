package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentDeleteServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentListDTO;
import java.util.List;

public interface CommentService {

	CommentCreateResponse createComment(CommentCreateServiceRequest request);

	List<CommentListDTO> getBoardComments(Long boardId);

	void updateComment(Long commentId, CommentUpdateServiceRequest request);

	void deleteComment(Long commentId, CommentDeleteServiceRequest request);

}
