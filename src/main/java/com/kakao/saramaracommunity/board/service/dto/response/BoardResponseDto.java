package com.kakao.saramaracommunity.board.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.kakao.saramaracommunity.board.entity.CategoryBoard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReadOneBoardResponseDto {

        private Long boardId;

        private String title;

        private String content;

        private CategoryBoard categoryBoard;

        private String memberNickname;

        private String memberEmail;

        private Long boardCnt;

        private Long likeCnt;

        private LocalDateTime deadLine;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReadAllBoardResponseDto {

        private String title;

        private String memberNickname;

        private Long boardCnt;

        private Long likeCnt;

        private LocalDateTime deadLine;
    }

    @Getter
    @Builder
    public static class ReadPageBoardResponseDto {
        private final List<ReadAllBoardResponseDto> values;

        private final Boolean hasNext;

        private final Long cursorId;

        public ReadPageBoardResponseDto(List<ReadAllBoardResponseDto> values, Boolean hasNext, Long cursorId) {
            this.values = values;
            this.hasNext = hasNext;
            this.cursorId = cursorId;
        }
    }
}
