package com.kakao.saramaracommunity.board.controller.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;

import com.kakao.saramaracommunity.board.service.dto.request.BoardServiceRequestDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class BoardRequestDto {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class SaveRequestDto {

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

        @Builder
        private SaveRequestDto(String title, String content, CategoryBoard categoryBoard, Long memberId, LocalDateTime deadLine) {
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.memberId = memberId;
            this.deadLine = deadLine;
        }

        public BoardServiceRequestDto.SaveRequestDto toServiceRequest() {
            return BoardServiceRequestDto.SaveRequestDto.builder()
                    .title(title)
                    .content(content)
                    .categoryBoard(categoryBoard)
                    .memberId(memberId)
                    .deadLine(deadLine)
                    .build();
        }

    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UpdateRequestDto {

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

        @Builder
        private UpdateRequestDto(Long memberId, String title, String content, CategoryBoard categoryBoard, LocalDateTime deadLine) {
            this.memberId = memberId;
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.deadLine = deadLine;
        }

        public BoardServiceRequestDto.UpdateRequestDto toServiceRequest() {
            return BoardServiceRequestDto.UpdateRequestDto.builder()
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .categoryBoard(categoryBoard)
                    .deadLine(deadLine)
                    .build();
        }

    }
}