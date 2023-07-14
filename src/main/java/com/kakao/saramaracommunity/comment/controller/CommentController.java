package com.kakao.saramaracommunity.comment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kakao.saramaracommunity.comment.dto.CommentListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Log4j2
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentDTO commentDTO){
		Long comment = commentService.createComment(commentDTO);

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

	@PutMapping("/{commentId}")
	public ResponseEntity<Map<String, Object>> updateComment(@Valid @PathVariable("commentId") Long commentId,
															 @Valid @RequestBody CommentDTO commentDTO){

		Boolean updatedComment = commentService.updateComment(commentId, commentDTO);

		Map<String, Object> result = new HashMap<>();

		result.put("result", updatedComment);

		return ResponseEntity.ok(result);
	}
}