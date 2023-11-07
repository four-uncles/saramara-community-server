package com.kakao.saramaracommunity.comment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequset;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequset;
import com.kakao.saramaracommunity.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentCreateRequset requset){
		Long comment = commentService.createComment(requset.toServiceRequest());
		Map<String, Object> result = new HashMap<>();
		result.put("result", comment);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{boardId}/comments")
	public ResponseEntity<Map<String, Object>> getBoardComments(@Valid @PathVariable("boardId") Long boardId) {
		List<CommentListDTO> boardComments = commentService.getBoardComments(boardId);
		Map<String, Object> result = new HashMap<>();
		result.put("result", boardComments);
		return ResponseEntity.ok(result);
	}

	// 전체 리소스를 수정할 필요가 없기 때문에 PUT보다 PATCH를 사용하는 것이 좋을 수 있다.
	@PutMapping("/{commentId}")
	public ResponseEntity<Map<String, Object>> updateComment(
			@Valid @PathVariable("commentId") Long commentId,
			@Valid @RequestBody CommentUpdateRequset requset
	){
		Boolean updatedComment = commentService.updateComment(commentId, requset.toServiceRequest());
		Map<String, Object> result = new HashMap<>();
		result.put("result", updatedComment);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Map<String, Object>> deleteComment(@Valid @PathVariable("commentId") Long commentId) {
		Boolean deletedComment = commentService.deleteComment(commentId);
		Map<String, Object> result = new HashMap<>();
		result.put("result", deletedComment);
		return ResponseEntity.ok(result);
	}
}