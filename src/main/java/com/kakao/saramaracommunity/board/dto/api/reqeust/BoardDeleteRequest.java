package com.kakao.saramaracommunity.board.dto.api.reqeust;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardDeleteServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BoardDeleteRequest(
        @NotNull(message = "존재하지 않는 사용자입니다.")
        Long memberId
) {
    public BoardDeleteServiceRequest toServiceRequest() {
        return BoardDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }
}
