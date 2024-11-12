package com.kakao.saramaracommunity.board.service.reqeust;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardCreateServiceRequest(
        String title,
        String content,
        CategoryBoard categoryBoard,
        Long memberId,
        LocalDateTime deadLine,
        List<String> boardImages
) {

    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .categoryBoard(categoryBoard)
                .member(member)
                .deadLine(deadLine)
                .images(boardImages)
                .build();
    }
}
