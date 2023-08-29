package com.kakao.saramaracommunity.board.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.*;

import java.time.LocalDateTime;

public class BoardServiceRequestDto {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class SaveRequestDto {
        private String title;
        private String content;
        private CategoryBoard categoryBoard;
        private Long memberId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime deadLine;

        @Builder
        private SaveRequestDto(String title, String content, CategoryBoard categoryBoard, Long memberId, LocalDateTime deadLine) {
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.memberId = memberId;
            this.deadLine = deadLine;
        }

    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UpdateRequestDto {

        private Long memberId;
        private String title;
        private String content;
        private CategoryBoard categoryBoard;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime deadLine;

        @Builder
        private UpdateRequestDto(Long memberId, String title, String content, CategoryBoard categoryBoard, LocalDateTime deadLine) {
            this.memberId = memberId;
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.deadLine = deadLine;
        }
    }
}