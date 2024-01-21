package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentDeleteServiceRequest;
import jakarta.validation.constraints.NotNull;

public record CommentDeleteRequest(
        @NotNull(message = "회원정보은 필수입니다.") Long memberId
) {

    public static CommentDeleteRequest of(Long memberId) {
        return CommentDeleteRequest.of(memberId);
    }

    public CommentDeleteServiceRequest toServiceRequest() {
        return CommentDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
