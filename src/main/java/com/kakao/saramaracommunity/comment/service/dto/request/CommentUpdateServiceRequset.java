package com.kakao.saramaracommunity.comment.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateServiceRequset {
    private Long commentId;
    private Long memberId;
    private Long boardId;
    private String content;
    private Long pick;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder
    private CommentUpdateServiceRequset(Long commentId, Long memberId, Long boardId, String content, Long pick, LocalDateTime regDate, LocalDateTime modDate) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.boardId = boardId;
        this.content = content;
        this.pick = pick;
        this.regDate = regDate;
        this.modDate = modDate;
    }

}
