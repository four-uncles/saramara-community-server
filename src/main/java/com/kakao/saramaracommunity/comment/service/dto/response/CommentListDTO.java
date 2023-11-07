package com.kakao.saramaracommunity.comment.service.dto.response;

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

    // 댓글로 선택한 이미지의 경우 최대로 쳐도 int 형의 범위를 벗어나지 않기에 int형으로 사용해도 무방하다.
    private Long pick;
}