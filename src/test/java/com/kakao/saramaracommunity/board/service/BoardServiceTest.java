// package com.kakao.saramaracommunity.board.service;
//
// import com.kakao.saramaracommunity.board.dto.BoardDTO;
// import com.kakao.saramaracommunity.board.entity.CategoryBoard;
//
// import jakarta.transaction.Transactional;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
//
// @Transactional
// @SpringBootTest
// public class BoardServiceTest {
//     @Autowired
//     private BoardService boardService;
//
//     @Test
//     public void 게시물_등록() {
//         //given
//         BoardDTO boardDTO = BoardDTO.builder()
//                 .category(CategoryBoard.NORMAL)
//                 .title("게시물 제목을 등록합니다.")
//                 .content("게시물 내용을 등록합니다.")
//                 .writerEmail("lango@kakao.com")
//                 .writerNickName("lango")
//                 .build();
//         //when
//         Long bno = boardService.boardRegister(boardDTO);
//         //then
//         System.out.println(bno);
//         System.out.println(boardDTO);
//     }
// }