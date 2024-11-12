package com.kakao.saramaracommunity.board.service.reqeust;

import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardUpdateServiceRequest(
        String title,
        String content,
        CategoryBoard categoryBoard,
        Long memberId,
        LocalDateTime deadLine,
        List<String> boardImages
) {

}
