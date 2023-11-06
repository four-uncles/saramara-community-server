package com.kakao.saramaracommunity.board.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.kakao.saramaracommunity.board.entity.AttachBoard;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;

import lombok.*;

@Getter
public class BoardResponse {

    @Getter
    public static class BoardGetResponse {
        private final Long boardId;
        private final String title;
        private final String content;
        private final CategoryBoard categoryBoard;
        private final String writer;
        private final String memberEmail;
        private final Long viewCount;
        private final Long likeCount;
        private final LocalDateTime deadLine;
        private final List<String> boardImages;

        @Builder
        private BoardGetResponse(
                Long boardId,
                String title,
                String content,
                CategoryBoard categoryBoard,
                String writer,
                String memberEmail,
                Long viewCount,
                Long likeCount,
                LocalDateTime deadLine,
                List<String> boardImages
        ) {
            this.boardId = boardId;
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.writer = writer;
            this.memberEmail = memberEmail;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.deadLine = deadLine;
            this.boardImages = boardImages;
        }

        public static BoardGetResponse of(Board board) {
            return BoardGetResponse.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .categoryBoard(board.getCategoryBoard())
                    .writer(board.getMember().getNickname())
                    .memberEmail(board.getMember().getEmail())
                    .viewCount(board.getViewCount())
                    .likeCount(board.getLikeCount())
                    .deadLine(board.getDeadLine())
                    .boardImages(board.getAttachBoards().stream().
                            map(AttachBoard::getImagePath)
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Getter
    public static class BoardSearchResponse {
        private final List<BoardGetResponse> boards;
        private final Boolean hasNext;
        private final Long cursorId;

        @Builder
        private BoardSearchResponse(
                List<BoardGetResponse> boards,
                Boolean hasNext,
                Long cursorId
        ) {
            this.boards = boards;
            this.hasNext = hasNext;
            this.cursorId = cursorId;
        }

        public static BoardSearchResponse of(List<BoardGetResponse> boards, Boolean hasNext, Long nextCursorId) {
            return BoardSearchResponse.builder()
                    .boards(boards)
                    .hasNext(hasNext)
                    .cursorId(nextCursorId)
                    .build();
        }
    }

    @Getter
    public static class BoardCreateResponse {
        private final Long boardId;
        private final String title;
        private final String content;
        private final CategoryBoard categoryBoard;
        private final String writer;
        private final Long viewCount;
        private final Long likeCount;
        private final LocalDateTime deadLine;
        private final List<String> boardImages;

        @Builder
        private BoardCreateResponse(
                Long boardId,
                String title,
                String content,
                CategoryBoard categoryBoard,
                String writer,
                Long viewCount,
                Long likeCount,
                LocalDateTime deadLine,
                List<String> boardImages
        ) {
            this.boardId = boardId;
            this.title = title;
            this.content = content;
            this.categoryBoard = categoryBoard;
            this.writer = writer;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.deadLine = deadLine;
            this.boardImages = boardImages;
        }

        public static BoardCreateResponse of(Board board) {
            return BoardCreateResponse.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .categoryBoard(board.getCategoryBoard())
                    .writer(board.getMember().getNickname())
                    .viewCount(board.getViewCount())
                    .likeCount(board.getLikeCount())
                    .deadLine(board.getDeadLine())
                    .boardImages(board.getAttachBoards().stream()
                            .map(AttachBoard::getImagePath)
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }

}
