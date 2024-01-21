package com.kakao.saramaracommunity.board.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.service.dto.request.BoardServiceRequest;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardCreateRequest {

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        @NotNull(message = "카테고리가 선택되지 않았습니다.")
        private CategoryBoard categoryBoard;

        @NotNull(message = "사용자의 고유번호가 존재하지 않습니다.")
        private Long memberId;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Future(message = "유효 기간은 현재 시간 이후로 설정해야 합니다.")
        private LocalDateTime deadLine;

        private List<String> boardImages;

        @Builder
        private BoardCreateRequest(
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

        public BoardServiceRequest.BoardCreateServiceRequest toServiceRequest() {
            return BoardServiceRequest.BoardCreateServiceRequest.builder()
                    .title(title)
                    .content(content)
                    .categoryBoard(categoryBoard)
                    .memberId(memberId)
                    .deadLine(deadLine)
                    .boardImages(boardImages)
                    .build();
        }

    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class BoardUpdateRequest {

        @NotNull(message = "사용자의 고유번호가 존재하지 않습니다.")
        private Long memberId;

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        @NotNull(message = "카테고리가 선택되지 않았습니다.")
        private CategoryBoard categoryBoard;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Future(message = "유효 기간은 현재 시간 이후로 설정해야 합니다.")
        private LocalDateTime deadLine;

        private List<String> boardImages;

        @Builder
        private BoardUpdateRequest(
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

        public BoardServiceRequest.BoardUpdateServiceRequest toServiceRequest() {
            return BoardServiceRequest.BoardUpdateServiceRequest.builder()
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .categoryBoard(categoryBoard)
                    .deadLine(deadLine)
                    .boardImages(boardImages)
                    .build();
        }
    }
}