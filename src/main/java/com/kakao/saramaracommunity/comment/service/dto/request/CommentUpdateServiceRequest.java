package com.kakao.saramaracommunity.comment.service.dto.request;

import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.Builder;

@Builder
public record CommentUpdateServiceRequest(Long memberId, String content) {

    public Comment toEntity(Member member, String content) {
        return Comment.builder()
                .member(member)
                .content(content)
                .build();
    }

}
