package com.kakao.saramaracommunity.comment.service.dto.response;

import com.kakao.saramaracommunity.comment.entity.Comment;
import java.util.List;

public record CommentsReadInBoardResponse(
        List<CommentReadDetailResponse> comments
) {

    public static CommentsReadInBoardResponse from(List<Comment> commentInBoard) {
        List<CommentReadDetailResponse> comments = commentInBoard.stream()
                .map(comment -> CommentReadDetailResponse.builder()
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
        return new CommentsReadInBoardResponse(comments);
    }

}
