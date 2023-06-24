package com.kakao.saramaracommunity.board.dto.request;

import com.kakao.saramaracommunity.board.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BoardRequestDto {

    @AllArgsConstructor
    @Getter
    @Builder
    public static class SaveRequestDto {

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        @NotNull(message = "카테고리를 선택해주세요.")
        private Category category;

        @NotNull(message = "사용자의 고유번호가 존재하지 않습니다.")
        private Long memberId;

        @NotNull(message = "사용자의 이메일가 존재하지 않습니다.")
        private String memberEmail;

        @NotNull(message = "사용자의 닉네임이 존재하지 않습니다.")
        private String memberNickname;
    }
}
