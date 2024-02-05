package com.kakao.saramaracommunity.comment.service.dto.request;

import java.util.Objects;
import lombok.Builder;

@Builder
public record CommentUpdateServiceRequest(Long memberId, String content) {

    public CommentUpdateServiceRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
        Objects.requireNonNull(content, "댓글 내용은 필수 입니다.");
    }

}
