package com.kakao.saramaracommunity.comment.controller;

import com.kakao.saramaracommunity.comment.controller.dto.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.controller.dto.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.comment.service.CommentService;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentCreateResponse;
import com.kakao.saramaracommunity.comment.service.dto.response.CommentsReadInBoardResponse;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
     * @param request - memberId: 작성자 고유 식별자, boardId: 게시글 고유 식별자, content: 내용
     * @return "code", "message", "data" : { "nickname", "content" }
     */
    @PostMapping("/register")
    public ApiResponse<CommentCreateResponse> createComment(
            @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentCreateResponse data = commentService.createComment(request.toServiceRequest());

        return ApiResponse.successResponse(
                HttpStatus.CREATED,
                "댓글 작성이 완료 되었습니다.",
                data
        );
    }

    @GetMapping("/{boardId}/comments")
    public ApiResponse<CommentsReadInBoardResponse> getBoardComments(
            @Valid @PathVariable("boardId") Long boardId
    ) {
        CommentsReadInBoardResponse data = commentService.readCommentsInBoard(boardId);
        return ApiResponse.successResponse(
                HttpStatus.OK,
                "댓글 조회가 완료 되었습니다.",
                data
        );
    }

    @PatchMapping("/{commentId}")
    public ApiResponse updateComment(
            @Valid @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        commentService.updateComment(commentId, request.toServiceRequest());
        return ApiResponse.successResponse(
                HttpStatus.OK,
                "댓글 수정이 완료 되었습니다.",
                true
        );
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse deleteComment(
            @Valid @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentDeleteRequest request
    ) {
        commentService.deleteComment(commentId, request.toServiceRequest());
        return ApiResponse.successResponse(
                HttpStatus.OK,
                "성공적으로 댓글을 삭제하였습니다.",
                true
        );
    }

}