package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @DisplayName("투표 타입의 게시글 생성시 게시글의 유형은 투표 유형으로 등록된다.")
    @Test
    void checkCategoryBoardIsVote() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member member = createMember();

        // when
        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();

        // then
        assertThat(board.getCategoryBoard()).isEqualTo(CategoryBoard.VOTE);
    }

    @DisplayName("찬반 타입의 게시글 생성시 게시글의 유형은 찬반 유형으로 등록된다.")
    @Test
    void checkCategoryBoardIsChoice() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Member member = createMember();

        // when
        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .categoryBoard(CategoryBoard.CHOICE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();

        // then
        assertThat(board.getCategoryBoard()).isEqualTo(CategoryBoard.CHOICE);
    }


    /**
     * 게시글 첨부목록 수정 관련 개발 미완료로 인해 임시로 주석 처리했음을 알립니다.
     */

//    @DisplayName("게시글 제목과 내용만 변경할 경우 변경할 게시글 정보만 변경한다.")
//    @Test
//    void updateBoardTitleAndContent() {
//        // given
//        List<String> attachPaths = List.of("image1.png", "image2.png");
//        Member member = createMember();
//        Board board = Board.builder()
//                .title("title")
//                .content("content")
//                .member(member)
//                .categoryBoard(CategoryBoard.VOTE)
//                .deadLine(LocalDateTime.now())
//                .attachPaths(attachPaths)
//                .build();
//
//        // when
//        board.update(
//                "new title",
//                "content is updated ..",
//                CategoryBoard.VOTE,
//                LocalDateTime.now(),
//                attachPaths
//        );
//
//        // then
//        assertThat(board.getTitle()).isEqualTo("new title");
//        assertThat(board.getContent()).isEqualTo("content is updated ..");
//        assertThat(board.getAttachBoards()).hasSize(2)
//                .extracting("imagePath")
//                .containsExactlyInAnyOrder("image1.png", "image2.png");
//    }

//    @DisplayName("게시글 첨부목록만 변경할 경우 변경할 게시글 정보만 변경한다.")
//    @Test
//    void updateBoardAttachBoards() {
//        // given
//        List<String> attachPaths = List.of("image1.png", "image2.png");
//        Member member = createMember();
//        Board board = Board.builder()
//                .title("title")
//                .content("content")
//                .member(member)
//                .categoryBoard(CategoryBoard.VOTE)
//                .deadLine(LocalDateTime.now())
//                .attachPaths(attachPaths)
//                .build();
//        List<String> newAttachPaths = List.of("image1.png", "image2.png", "image3.png");
//        // when
//        board.update(
//                "new title",
//                "content is updated ..",
//                CategoryBoard.VOTE,
//                LocalDateTime.now(),
//                newAttachPaths
//        );
//
//        // then
//        assertThat(board.getTitle()).isEqualTo("new title");
//        assertThat(board.getContent()).isEqualTo("content is updated ..");
//        assertThat(board.getAttachBoards()).hasSize(3)
//                .extracting("imageOrder", "imagePath")
//                .containsExactlyInAnyOrder(
//                        tuple("image1.png", 1),
//                        tuple("image2.png", 2),
//                        tuple("image3.png", 3)
//                );
//    }

    private Member createMember() {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password("test")
                .type(Type.LOCAL)
                .build();
    }
}