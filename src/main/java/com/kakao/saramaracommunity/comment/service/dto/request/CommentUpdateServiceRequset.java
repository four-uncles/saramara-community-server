package com.kakao.saramaracommunity.comment.service.dto.request;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateServiceRequset {

    private Long commentId;
    private Long memberId;
    private Long boardId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder
    private CommentUpdateServiceRequset(Long commentId, Long memberId, Long boardId, String content,
            LocalDateTime regDate, LocalDateTime modDate) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.boardId = boardId;
        this.content = content;
        this.regDate = regDate;
        this.modDate = modDate;
    }

}
