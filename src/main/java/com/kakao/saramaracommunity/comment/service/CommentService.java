package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequset;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentListDTO;
import java.util.List;

public interface CommentService {

	CommentCreateResponse createComment(CommentCreateServiceRequest request);

	List<CommentListDTO> getBoardComments(Long boardId);

	Boolean updateComment(Long commentId, CommentUpdateServiceRequset requset);

	Boolean deleteComment(Long commentId);

}
