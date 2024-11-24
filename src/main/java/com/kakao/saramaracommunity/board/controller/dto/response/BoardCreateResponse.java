package com.kakao.saramaracommunity.board.controller.dto.response;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardCreateResponse(
        Long boardId,
        String title,
        String content,
        CategoryBoard categoryBoard,
        String writer,
        LocalDateTime deadLine,
        List<String> boardImages
) {
    public static BoardCreateResponse of(Board board) {
        return BoardCreateResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryBoard(board.getCategoryBoard())
                .writer(board.getMember().getNickname())
                .deadLine(board.getDeadLine())
                .boardImages(board.getBoardImages().stream()
                        .map(BoardImage::getPath)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
