package com.kakao.saramaracommunity.comment.service.dto.request;

import java.util.Objects;
import lombok.Builder;

@Builder
public record CommentDeleteServiceRequest(Long memberId) {

    public CommentDeleteServiceRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
    }

}
