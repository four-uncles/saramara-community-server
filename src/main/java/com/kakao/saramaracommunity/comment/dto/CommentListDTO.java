package com.kakao.saramaracommunity.comment.dto;

import lombok.*;

/**
 * 특정 Board에 대해 댓글을 불러올 때 사용될 DTO입니다.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentListDTO {

    private Long commentId;

    private String nickname;

    private String content;

    private Long pick;
}