package com.kakao.saramaracommunity.board.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
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
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
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
    }
}