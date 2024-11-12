package com.kakao.saramaracommunity.board.controller.dto.reqeust;

import com.kakao.saramaracommunity.board.service.reqeust.BoardDeleteServiceRequest;
import com.kakao.saramaracommunity.common.dto.ConvertDtoByBusinessLayer;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BoardDeleteRequest(
        @NotNull(message = "존재하지 않는 사용자입니다.")
        Long memberId
) implements ConvertDtoByBusinessLayer<BoardDeleteServiceRequest> {

    @Override
    public BoardDeleteServiceRequest toServiceRequest() {
        return BoardDeleteServiceRequest.builder()
                .memberId(memberId)
                .build();
    }

}
