package com.kakao.saramaracommunity.comment.controller;

import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.service.CommentService;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> createComment(@Valid @RequestBody CommentCreateRequest request){
		commentService.createComment(request.toServiceRequest());

		return ResponseEntity.ok().body(
				ApiResponse.of(
						HttpStatus.OK,
						"댓글 작성이 완료 되었습니다."
				)
		);
	}

//	@GetMapping("/{boardId}/comments")
//	public ResponseEntity<Map<String, Object>> getBoardComments(@Valid @PathVariable("boardId") Long boardId) {
//		List<CommentListDTO> boardComments = commentService.getBoardComments(boardId);
//		Map<String, Object> result = new HashMap<>();
//		result.put("result", boardComments);
//		return ResponseEntity.ok(result);
//	}

	@PatchMapping("/{commentId}")
	public ResponseEntity<ApiResponse> updateComment(
			@Valid @PathVariable("commentId") Long commentId,
			@Valid @RequestBody CommentUpdateRequest request
	){
		commentService.updateComment(commentId, request.toServiceRequest());
		return ResponseEntity.ok().body(
				ApiResponse.of(
						HttpStatus.OK,
						"댓글 수정이 완료 되었습니다."
				)
		);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(
			@Valid @PathVariable("commentId") Long commentId,
			@Valid @RequestBody CommentDeleteRequest request
    ) {
		commentService.deleteComment(commentId, request.toServiceRequest());
		return ResponseEntity.ok().body(
                ApiResponse.of(
                        HttpStatus.OK,
                        "성공적으로 댓글을 삭제하였습니다."
                )
        );
	}

}