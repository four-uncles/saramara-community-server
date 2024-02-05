package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import java.util.Objects;

public record CommentCreateRequest(Long memberId, Long boardId, String content) {

    public CommentCreateRequest {
        Objects.requireNonNull(memberId, "회원 정보는 필수 입니다.");
        Objects.requireNonNull(boardId, " 게시글 정보는 필수 입니다.");
        Objects.requireNonNull(content, " 댓글 내용은 필수 입니다.");
    }

    public CommentCreateServiceRequest toServiceRequest() {
        return CommentCreateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
    }

}
