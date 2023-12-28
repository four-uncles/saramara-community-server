package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public class BoardFixture {

    public static Board createBoard(Member member, List<String> attachPaths) {
        return Board.builder()
                .title("집에서 잠옷으로 입을 옷을 골라주세요.")
                .content("content")
                .member(member)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();
    }

}
