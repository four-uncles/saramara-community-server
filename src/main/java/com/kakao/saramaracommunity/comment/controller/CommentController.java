package com.kakao.saramaracommunity.comment.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}