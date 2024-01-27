package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentDeleteServiceRequest;
import java.util.Objects;

public record CommentDeleteRequest(
        Long memberId
) {

    public CommentDeleteRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
    }

    public CommentDeleteServiceRequest toServiceRequest() {
        return CommentDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
