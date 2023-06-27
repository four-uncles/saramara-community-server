package com.kakao.saramaracommunity.comment.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long commentId;

    @NotNull(message = "회원이 없습니다.")
    private Long memberId;

    @NotNull(message = "게시판이 없습니다.")
    private Long boardId;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "투표를 선택해주세요.")
    private Long pick;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
