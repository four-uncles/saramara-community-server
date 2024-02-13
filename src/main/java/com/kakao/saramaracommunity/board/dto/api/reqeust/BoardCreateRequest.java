package com.kakao.saramaracommunity.board.dto.api.reqeust;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardCreateRequest(
        @NotBlank(message = "게시글의 제목을 입력해주세요.")
        String title,
        @NotBlank(message = "게시글의 내용을 입력해주세요.")
        String content,
        @NotNull(message = "게시글의 카테고리가 선택되지 않았습니다.")
        CategoryBoard categoryBoard,
        @NotNull(message = "존재하지 않는 사용자입니다.")
        Long memberId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Future(message = "게시글의 마감 기한은 현재 시간 이후로 설정해야 합니다.")
        LocalDateTime deadLine,
        List<String> boardImages
) {

    public BoardCreateServiceRequest toServiceReqeust() {
        return BoardCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .categoryBoard(categoryBoard)
                .memberId(memberId)
                .deadLine(deadLine)
                .boardImages(boardImages)
                .build();
    }

}
