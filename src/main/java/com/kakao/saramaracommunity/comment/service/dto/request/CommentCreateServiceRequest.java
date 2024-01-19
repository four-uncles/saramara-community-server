package com.kakao.saramaracommunity.comment.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateServiceRequest {

    private Long commentId;
    private Long memberId;
    private Long boardId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder
    private CommentCreateServiceRequest(Long commentId, Long memberId, Long boardId, String content,
            LocalDateTime regDate, LocalDateTime modDate) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.boardId = boardId;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
    }

}
