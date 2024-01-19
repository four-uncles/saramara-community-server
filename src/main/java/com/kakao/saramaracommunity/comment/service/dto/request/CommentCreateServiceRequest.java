package com.kakao.saramaracommunity.comment.service.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateServiceRequest(
        Long memberId,
        Long boardId,
        String content
) {

}
