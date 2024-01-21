package com.kakao.saramaracommunity.comment.service.dto.request;

import lombok.Builder;

@Builder
public record CommentUpdateServiceRequest(Long memberId, Long boardId, String content) {

    public static CommentUpdateServiceRequest of(Long memberId, Long boardId, String content) {
        return CommentUpdateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
    }

}
