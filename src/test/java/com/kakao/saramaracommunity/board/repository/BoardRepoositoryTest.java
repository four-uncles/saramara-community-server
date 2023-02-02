package com.kakao.saramaracommunity.board.repository;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.LongStream;

@SpringBootTest
@Log4j2
public class BoardRepoositoryTest {
    @Autowired
    private BoardRepository boardRepository;
    @Test
    public void 게시판등록() {
        LongStream.rangeClosed(1, 100).forEach(i->{
            Board board = Board.builder()
                    .title("Sample 제목[" + i + "]")
                    .content("Sample 내용[" + i + "]")
                    .likes((long)(Math.random() * 100) + 1)
                    .build();
            board.addCate(CategoryBoard.QUESTION);
            if (i > 90) {
                board.addCate(CategoryBoard.NORMAL);
            }
            boardRepository.save(board);
        });
    }
}
