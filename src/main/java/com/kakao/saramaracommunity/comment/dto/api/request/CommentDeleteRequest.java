package com.kakao.saramaracommunity.comment.dto.api.request;

import com.kakao.saramaracommunity.comment.dto.business.request.CommentDeleteServiceRequest;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record CommentDeleteRequest(
        @NotNull(message = "회원 정보는 필수 입니다.") Long memberId
) {

    public CommentDeleteServiceRequest toServiceRequest() {
        return CommentDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
