package com.kakao.saramaracommunity.board.dto.business.response;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardGetResponse(
        Long boardId,
        String title,
        String content,
        CategoryBoard categoryBoard,
        String writerNickname,
        String writerEmail,
        LocalDateTime deadLine,
        List<String> boardImages
) {
    public static BoardGetResponse of(Board board) {
        return BoardGetResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryBoard(board.getCategoryBoard())
                .writerNickname(board.getMember().getNickname())
                .writerEmail(board.getMember().getEmail())
                .deadLine(board.getDeadLine())
                .boardImages(board.getBoardImages().stream().
                        map(BoardImage::getPath)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
