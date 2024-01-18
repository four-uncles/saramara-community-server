package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.fixture.MemberFixture;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    /**
     * create
     * 게시글 등록 관련 테스트 케이스
     */
    @DisplayName("찬반 타입의 게시글 생성시 찬반 카테고리의 게시글로 등록된다.")
    @Test
    void test() {
        // given

        // when

        // then
    }


    @DisplayName("투표 타입의 게시글 생성시 투표 카테고리의 게시글로 등록된다.")
    @Test
    void checkCategoryBoardIsVote() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3");

        // when
        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        // then
        assertThat(board.getCategoryBoard()).isEqualTo(CategoryBoard.VOTE);
    }


    /**
     * update
     * 게시글 수정 관련 테스트 케이스
     */

    @DisplayName("게시글의 게시글 이미지 한장을 수정한다.")
    @Test
    void updateBoard() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> createImages = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(createImages)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);

        List<String> updateImages = List.of(
                "update-path1",
                "path2",
                "path3"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(3)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "update-path1", "path2", "path3"
                );
    }

    @DisplayName("게시글의 게시글 이미지 한장을 새로 추가한다.")
    @Test
    void updateBoardAddImage() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);

        List<String> updateImages = List.of(
                "path1",
                "path2",
                "path3",
                "path4"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        ReflectionTestUtils.setField(board.getBoardImages().get(3), "id", 4L);

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(4)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path1", "path2", "path3", "path4"
                );
    }

    @DisplayName("게시글의 게시글 이미지 두 장을 새로 추가한다.")
    @Test
    void updateBoardAddTwoImage() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);

        List<String> updateImages = List.of(
                "path1",
                "path2",
                "path3",
                "path4",
                "path5"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        ReflectionTestUtils.setField(board.getBoardImages().get(3), "id", 4L);
        ReflectionTestUtils.setField(board.getBoardImages().get(4), "id", 5L);

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(5)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path1", "path2", "path3", "path4", "path5"
                );
    }

    @DisplayName("게시글의 등록된 기존 게시글 이미지 한장을 삭제한 후, 게시글을 수정한다.")
    @Test
    void deleteBoardWithImageRemove() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);

        List<String> updateImages = List.of(
                "path2",
                "path3"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(2)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path2", "path3"
                );
    }

    @DisplayName("게시글의 등록된 기존 게시글 이미지 두 장을 삭제한 후, 게시글을 수정한다.")
    @Test
    void deleteBoardWithTwoImageRemove() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);

        List<String> updateImages = List.of(
                "path2"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(1)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path2"
                );
    }

    @DisplayName("게시글의 등록된 기존 게시글 이미지 다섯 장 중 두 장을 삭제한 후, 게시글을 수정한다.")
    @Test
    void deleteBoardWithTwoImageRemoveWhenFiveImage() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = List.of("path1", "path2", "path3", "path4", "path5");

        Board board = Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();

        ReflectionTestUtils.setField(board.getBoardImages().get(0), "id", 1L);
        ReflectionTestUtils.setField(board.getBoardImages().get(1), "id", 2L);
        ReflectionTestUtils.setField(board.getBoardImages().get(2), "id", 3L);
        ReflectionTestUtils.setField(board.getBoardImages().get(3), "id", 4L);
        ReflectionTestUtils.setField(board.getBoardImages().get(4), "id", 5L);

        List<String> updateImages = List.of(
                "path2",
                "path4"
        );

        // when
        board.update(
                "update-title",
                "update-content",
                CategoryBoard.VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(2)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path2", "path4"
                );
    }

    /**
     * delete
     * 게시글 삭제 관련 테스트 케이스
     */




//    @DisplayName("찬반 타입의 게시글 생성시 게시글의 유형은 찬반 유형으로 등록된다.")
//    @Test
//    void checkCategoryBoardIsChoice() {
//        // given
//        List<String> attachPaths = List.of("image1.png", "image2.png");
//        Member member = createMember();
//
//        // when
//        Board board = Board.builder()
//                .title("title")
//                .content("content")
//                .member(member)
//                .categoryBoard(CategoryBoard.CHOICE)
//                .deadLine(LocalDateTime.now())
//                .images(attachPaths)
//                .build();
//
//        // then
//        assertThat(board.getCategoryBoard()).isEqualTo(CategoryBoard.CHOICE);
//    }


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
//                .extracting("path")
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
//                .extracting("id", "path")
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
                .build();
    }
}