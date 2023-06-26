// package com.kakao.saramaracommunity.board.dto;
//
// import com.kakao.saramaracommunity.board.entity.Board;
// import com.kakao.saramaracommunity.board.entity.CategoryBoard;
// import com.kakao.saramaracommunity.member.entity.Member;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
//
// @SpringBootTest
// public class BoardDTOTest {
//     @Test
//     public void DTO_검증() {
//         Member member = Member.builder()
//                 .email("lango@kakao.com")
//                 .nickname("lango")
//                 .build();
//         Board board = Board.builder()
//                 .title("DTO 검증")
//                 .content("DTO 검증 테스트 중")
//                 .categoryBoard(CategoryBoard.NORMAL)
//                 .member(member)
//                 .build();
//         BoardDTO boardDTO = BoardDTO.builder()
//                 .writerEmail(board.getMember().getEmail())
//                 .writerNickName(board.getMember().getNickname())
//                 .title(board.getTitle())
//                 .content(board.getContent())
//                 .categoryBoard(board.getCategoryBoard())
//                 .build();
//         System.out.println(boardDTO);
//     }
// }
