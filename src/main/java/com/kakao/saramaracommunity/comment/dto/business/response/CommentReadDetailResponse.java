package com.kakao.saramaracommunity.comment.dto.business.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentReadDetailResponse(
        String nickname,
        String content,
        LocalDateTime createdAt
) {

}
