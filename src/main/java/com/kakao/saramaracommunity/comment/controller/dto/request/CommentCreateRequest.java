package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull(message = "회원정보은 필수입니다.") Long memberId,
        @NotNull(message = "게시글 번호는 필수입니다.") Long boardId,
        @NotBlank(message = "댓글 내용은 필수입니다.") String content
) {

    public CommentCreateServiceRequest toServiceRequest() {
        return CommentCreateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
    }
}
