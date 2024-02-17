package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.dto.business.request.CommentCreateServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.request.CommentDeleteServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.request.CommentUpdateServiceRequest;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentsReadInBoardResponse;

public interface CommentService {

	CommentCreateResponse createComment(CommentCreateServiceRequest request);

	CommentsReadInBoardResponse readCommentsInBoard(Long boardId);

	void updateComment(Long commentId, CommentUpdateServiceRequest request);

	void deleteComment(Long commentId, CommentDeleteServiceRequest request);

}
