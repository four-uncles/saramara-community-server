package com.kakao.saramaracommunity.comment.dto.api.request;

import com.kakao.saramaracommunity.comment.dto.business.request.CommentCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record CommentCreateRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId,
        @NotNull(message = "게시글 정보는 필수 입니다.") Long boardId,
        @NotBlank(message = "댓글 내용은 필수 입니다.") String content
) {

    public CommentCreateServiceRequest toServiceRequest() {
        return CommentCreateServiceRequest.builder()
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .build();
    }

}
