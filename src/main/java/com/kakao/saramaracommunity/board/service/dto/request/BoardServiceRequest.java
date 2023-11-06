package com.kakao.saramaracommunity.board.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class BoardServiceRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardCreateServiceRequest {
        private String title;
        private String content;
        private CategoryBoard categoryBoard;
        private Long memberId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime deadLine;
        private List<String> boardImages;

        @Builder
        private BoardCreateServiceRequest(
                String title,
                String content,
                CategoryBoard categoryBoard,
                Long memberId,
                LocalDateTime deadLine,
                List<String> boardImages
        ) {
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.memberId = memberId;
            this.deadLine = deadLine;
            this.boardImages = boardImages;
        }

        public Board toEntity(Member member) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .categoryBoard(categoryBoard)
                    .member(member)
                    .deadLine(deadLine)
                    .attachPaths(boardImages)
                    .build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class BoardUpdateServiceRequest {

        private Long memberId;
        private String title;
        private String content;
        private CategoryBoard categoryBoard;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime deadLine;
        private List<String> boardImages;

        @Builder
        private BoardUpdateServiceRequest(
                Long memberId,
                String title,
                String content,
                CategoryBoard categoryBoard,
                LocalDateTime deadLine,
                List<String> boardImages
        ) {
            this.memberId = memberId;
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.deadLine = deadLine;
            this.boardImages = boardImages;
        }
    }
}