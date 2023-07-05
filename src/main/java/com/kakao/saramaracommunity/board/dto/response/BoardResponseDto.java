package com.kakao.saramaracommunity.board.dto.response;

import java.time.LocalDateTime;

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
}
