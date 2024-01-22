package com.kakao.saramaracommunity.comment.service.dto.request;

import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.Builder;

@Builder
public record CommentDeleteServiceRequest(Long memberId) {

    public Comment toEntity(Member member) {
        return Comment.builder()
                .member(member)
                .build();
    }

}
