package com.kakao.saramaracommunity.comment.service.dto.response;

import com.kakao.saramaracommunity.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long id,
        String content
) {

    public static CommentCreateResponse of(Comment comment) {
        return CommentCreateResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

}
