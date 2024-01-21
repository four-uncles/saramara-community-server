package com.kakao.saramaracommunity.comment.service.dto.request;

import lombok.Builder;

@Builder
public record CommentDeleteServiceRequest(Long memberId) {

    public static CommentDeleteServiceRequest of(Long memberId) {
        return CommentDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
