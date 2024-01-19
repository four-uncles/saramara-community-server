package com.kakao.saramaracommunity.comment.service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

}