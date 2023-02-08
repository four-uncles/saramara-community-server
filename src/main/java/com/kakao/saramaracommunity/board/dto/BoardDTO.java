package com.kakao.saramaracommunity.board.dto;

import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long boardId;
    private String writerEmail;
    private String writerNickName;
    private CategoryBoard category;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
