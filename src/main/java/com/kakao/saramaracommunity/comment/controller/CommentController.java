package com.kakao.saramaracommunity.comment.controller;

import com.kakao.saramaracommunity.comment.dto.api.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.service.CommentService;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.dto.business.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 댓글 작성 API
     *
     * @param request memberId: 작성자 고유 식별자, boardId: 게시글 고유 식별자, content: 내용
     * @return "code", "message", "data" : { "nickname", "content" }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentCreateResponse data = commentService.createComment(request.toServiceRequest());

        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 댓글을 작성 하였습니다.",
                        data
                )
        );
    }

    /**
     * 댓글 조회 API
     * @param boardId 댓글이 작성된 게시글 고유 식별자
     * @return "code", "message", "data" : List<CommentReadDetailResponse> comments - { nickname, content, createdAt }
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<CommentsReadInBoardResponse>> getBoardComments(
            @Valid @PathVariable("boardId") Long boardId
    ) {
        CommentsReadInBoardResponse data = commentService.readCommentsInBoard(boardId);
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 게시글의 댓글을 모두 조회 하였습니다.",
                        data
                )
        );
    }

    /**
     * 댓글 수정 API
     * @param commentId 작성된 댓글의 고유 식별자
     * @param request memberId: 작성자 고유 식별자, content: 수정할 내용
     * @return "code", "message", "data" : true
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(
            @Valid @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        commentService.updateComment(commentId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 댓글을 수정 하였습니다."
                )
        );
    }

    /**
     * 댓글 삭제 API
     * @param commentId 작성된 댓글의 고유 식별자
     * @param request memberId: 작성자의 고유 식별자
     * @return "code", "message", "data" : true
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @Valid @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentDeleteRequest request
    ) {
        commentService.deleteComment(commentId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 댓글을 삭제 하였습니다."
                )
        );
    }

}