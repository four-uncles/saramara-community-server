package com.kakao.saramaracommunity.comment.dto;

import jakarta.persistence.Column;
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

    private Long boardId;

    private String writerEmail;

    private String writerNickname;

    private String content;

    private Long likes;

    private Long pick;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
