package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentUpdateServiceRequest;
import java.util.Objects;

public record CommentUpdateRequest(Long memberId, String content) {

    public CommentUpdateRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
        Objects.requireNonNull(content, "댓글 내용은 필수 입니다.");
    }

    public CommentUpdateServiceRequest toServiceRequest() {
        return CommentUpdateServiceRequest.builder()
                .memberId(memberId)
                .content(content)
                .build();
    }

}
