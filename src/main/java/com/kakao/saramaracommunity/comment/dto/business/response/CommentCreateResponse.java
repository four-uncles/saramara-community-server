package com.kakao.saramaracommunity.comment.dto.business.response;

import com.kakao.saramaracommunity.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentCreateResponse(
        String nickname,
        String content
) {

    public static CommentCreateResponse of(Comment comment) {
        return CommentCreateResponse.builder()
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .build();
    }

}
