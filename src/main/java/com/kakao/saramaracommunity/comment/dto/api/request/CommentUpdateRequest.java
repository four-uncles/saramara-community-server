package com.kakao.saramaracommunity.comment.dto.api.request;

import com.kakao.saramaracommunity.comment.dto.business.request.CommentUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record CommentUpdateRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId,
        @NotBlank(message = "댓글 내용은 필수 입니다.") String content
) {

    public CommentUpdateServiceRequest toServiceRequest() {
        return CommentUpdateServiceRequest.builder()
                .memberId(memberId)
                .content(content)
                .build();
    }

}
