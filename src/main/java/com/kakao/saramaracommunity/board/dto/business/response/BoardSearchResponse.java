package com.kakao.saramaracommunity.board.dto.business.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BoardSearchResponse(
    List<BoardGetResponse> boards,
    Boolean hasNext,
    Long cursorId
) {
    public static BoardSearchResponse of(
            List<BoardGetResponse> boards,
            Boolean hasNext,
            Long cursorId
    ) {
        return BoardSearchResponse.builder()
                .boards(boards)
                .hasNext(hasNext)
                .cursorId(cursorId)
                .build();
    }
}
