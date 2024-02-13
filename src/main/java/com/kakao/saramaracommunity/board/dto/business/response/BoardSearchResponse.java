package com.kakao.saramaracommunity.board.dto.business.response;

import com.kakao.saramaracommunity.board.service.dto.response.BoardResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record BoardSearchResponse(
    List<BoardResponse.BoardGetResponse> boards,
    Boolean hasNext,
    Long cursorId
) {
    public static BoardSearchResponse of(
            List<BoardResponse.BoardGetResponse> boards,
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
