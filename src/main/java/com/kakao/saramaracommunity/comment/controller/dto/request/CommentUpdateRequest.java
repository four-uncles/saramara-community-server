package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentUpdateRequest(
        @NotNull(message = "회원정보은 필수입니다.") Long memberId,
        @NotBlank(message = "댓글 내용은 필수입니다.") String content
) {

    public CommentUpdateServiceRequest toServiceRequest() {
        return CommentUpdateServiceRequest.builder()
                .memberId(memberId)
                .content(content)
                .build();
    }

}
