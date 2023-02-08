package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void 댓글_등록() {

        // TODO : 트랜잭션 처리로 값 데이터 저장으로 뜨긴 하지만 실제 저장 값이 안나옴
        // given
        CommentDTO commentDTO = CommentDTO.builder()
                .boardId(1L)
                .writerNickname("Sonny")
                .writerEmail("sonny@gmail.com")
                .content("content...")
                .likes(0L)
                .pick(1L)
                .build();

        // when
        Long cno = commentService.register(commentDTO);

        // then
        System.out.println("cno = " + cno);
    }
}
