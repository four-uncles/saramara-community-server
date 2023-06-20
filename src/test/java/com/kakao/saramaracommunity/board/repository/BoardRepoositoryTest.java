// package com.kakao.saramaracommunity.board.repository;
//
// import com.kakao.saramaracommunity.board.entity.Board;
// import com.kakao.saramaracommunity.board.entity.CategoryBoard;
// import com.kakao.saramaracommunity.member.entity.Member;
// import com.kakao.saramaracommunity.member.repository.MemberRepository;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
//
// import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
// @SpringBootTest
// public class BoardRepoositoryTest {
//     @Autowired
//     private BoardRepository boardRepository;
//     @Autowired
//     MemberRepository memberRepository;
//
//     @AfterEach
//     public void clean() { boardRepository.deleteAll(); }
//
//     @Test
//     public void save(){
//         //given
//         Member memberId = memberRepository.getReferenceById(1L);
//         boardRepository.save(Board.builder()
//                         .member(memberId)
//                         .category(CategoryBoard.NORMAL)
//                         .title("게시판 등록 테스트")
//                         .content("게시판 등록 테스트")
//                         .build());
//         //when
//         List<Board> boardList = boardRepository.findAll();
//         //then
//         Board board = boardList.get(0);
//         assertThat(board.getId()).isEqualTo(board.getId());
//         assertThat(board.getTitle()).isEqualTo(board.getTitle());
//         assertThat(board.getContent()).isEqualTo(board.getContent());
//     }
//
//     @Test
//     public void delete(){
//         //given
//         Board board = boardRepository.save(Board.builder()
//                         .category(CategoryBoard.NORMAL)
//                         .title("삭제된 게시글 입니다.")
//                         .content("삭제된 게시글의 내용입니다.")
//                         .build());
//         Long boardId = board.getId();
//         boardRepository.deleteById(boardId);
//
//         //when
//         //게시글 조회시 목록에서 제외된다.
//         int allBoardCnt = (int) boardRepository.count();
//         System.out.println("all board cnt: " + allBoardCnt);
//
//         //then
//         assertThat(allBoardCnt).isEqualTo(0);
//
//     }
//
//     @Test
//     public void findAll() {
//         //given
//         Board board = boardRepository.save(Board.builder()
//                     .category(CategoryBoard.NORMAL)
//                     .title("게시판 등록 테스트")
//                     .content("게시판 등록 테스트")
//                     .build());
//
//         //when
//         List<Board> boardList = boardRepository.findAll();
//
//         //then
//         Board result = boardList.get(0);
//         assertThat(result.getId()).isEqualTo(board.getId());
//         assertThat(result.getTitle()).isEqualTo(board.getTitle());
//         assertThat(result.getContent()).isEqualTo(board.getContent());
//     }
//
//     @Test
//     public void findById() {
//         //given
//         Board board = boardRepository.save(Board.builder()
//                 .category(CategoryBoard.NORMAL)
//                 .title("게시판 등록 테스트")
//                 .content("게시판 등록 테스트")
//                 .build());
//
//         //when
//         Optional<Board> optional = boardRepository.findById(board.getId());
//         Board result = optional.get();
//
//         //then
//         assertThat(result.getId()).isEqualTo(board.getId());
//         assertThat(result.getTitle()).isEqualTo(board.getTitle());
//         assertThat(result.getContent()).isEqualTo(board.getContent());
//     }
//
//     @Test
//     public void update() {
//         //given
//         Board board = boardRepository.save(Board.builder()
//                 .category(CategoryBoard.NORMAL)
//                 .title("게시판 등록 테스트")
//                 .content("게시판 등록 테스트")
//                 .build());
//
//         Board updateBoard = Board.builder()
//                 .category(CategoryBoard.NORMAL)
//                 .title("게시판 수정 테스트")
//                 .content("게시판 수정 테스트")
//                 .build();
//
//         //when
//         List<Board> boardList = boardRepository.findAll();
//         Board result = boardList.get(0);
//
//         //then
//         assertThat(result.getId()).isEqualTo(board.getId());
//         assertThat(result.getTitle()).isEqualTo(board.getTitle());
//         assertThat(result.getContent()).isEqualTo(board.getContent());
//     }
//
//     //Board 데이터를 가져올 때 Writer의 데이터도 가져오기
//     @Test
//     public void joinWithMember(){
//         Object result = boardRepository.getBoardWithMember(1L);
//         System.out.println(result);
//         Object[] ar = (Object[]) result;
//         System.out.println(Arrays.toString(ar));
//         Board board = (Board) ar[0];
//         Member member = (Member) ar[1];
//     }
// }