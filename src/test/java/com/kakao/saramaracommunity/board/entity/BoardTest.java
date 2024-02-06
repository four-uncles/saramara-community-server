package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.exception.BoardErrorCode;
import com.kakao.saramaracommunity.fixture.MemberFixture;
import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.CHOICE;
import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 게시글(Board) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스입니다.
 * 추후 DCI 패턴을 통해 아래와 같은 계층구조로 테스트 코드를 작성할 계획입니다.
 */
class BoardTest {

    @DisplayName("찬반 타입의 게시글 생성 시, 찬반 카테고리의 게시글로 등록된다.")
    @Test
    void create_Board_With_Category_Is_Choice() {
        // given
        Member NORMAL_MEMBER = MemberFixture.NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(1);

        // when
        Board board = createBoard(NORMAL_MEMBER, images, CHOICE);

        // then
        assertThat(board.getCategoryBoard()).isSameAs(CHOICE);
    }

    @DisplayName("찬반 타입의 게시글을 생성 시, 이미지를 2장 이상 등록한다면 예외가 발생한다.")
    @Test
    void create_Board_With_Category_Is_Choice_When_Two_Images() {
        // given
        Member NORMAL_MEMBER = MemberFixture.NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(2);

        // when & then
        assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, CHOICE))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글 생성시 투표 카테고리의 게시글로 등록된다.")
    @Test
    void create_Board_With_Category_Is_Vote() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);

        // when
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);

        // then
        assertThat(board.getCategoryBoard()).isEqualTo(VOTE);
    }

    @DisplayName("투표 타입의 게시글을 생성 시, 1장의 이미지만 등록했다면 예외가 발생한다.")
    @Test
    void create_Board_With_Category_Is_Vote_When_One_Images() {
        // given
        Member NORMAL_MEMBER = MemberFixture.NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(1);

        // when & then
        assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, VOTE))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 생성 시, 이미지를 5장 초과하여 등록했다면 예외가 발생한다.")
    @Test
    void create_Board_With_Category_Is_Vote_When_Over_Five_Images() {
        // given
        Member NORMAL_MEMBER = MemberFixture.NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(6);

        // when & then
        assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, VOTE))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("찬반 타입의 게시글을 수정할 때, 이미지를 새로 추가한다.")
    @Test
    void update_Board_With_Category_Is_Choice_New_Image() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(1);
        Board board = createBoard(NORMAL_MEMBER, images, CHOICE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when
        List<String> updateImages = createImagePaths(1);
        board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                board.getCategoryBoard(),
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(1)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "s3-image-path-1"
                );
    }

    @DisplayName("찬반 타입의 게시글을 수정할 때, 이미지는 반드시 1장이어야 한다.")
    @Test
    void update_Board_With_Category_Is_Choice_When_Over_One_Image() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(1);
        Board board = createBoard(NORMAL_MEMBER, images, CHOICE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when & then
        List<String> updateImages = createImagePaths(3);
        assertThatThrownBy(() -> board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                board.getCategoryBoard(),
                LocalDateTime.now(),
                updateImages
        ))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 수정할 때, 이미지 1장을 새로 추가한다.")
    @Test
    void update_Board_With_Category_Is_Vote_New_Image() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when
        List<String> updateImages = createImagePaths(4);
        board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(4)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "s3-image-path-1",
                        "s3-image-path-2",
                        "s3-image-path-3",
                        "s3-image-path-4"
                );
    }

    @DisplayName("투표 타입의 게시글을 수정할 때, 이미지 2장을 새로 추가한다.")
    @Test
    void update_Board_With_Category_Is_Vote_New_Two_Image() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when
        List<String> updateImages = createImagePaths(5);
        board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(5)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "s3-image-path-1",
                        "s3-image-path-2",
                        "s3-image-path-3",
                        "s3-image-path-4",
                        "s3-image-path-5"
                );
    }

    @DisplayName("투표 타입의 게시글을 수정하여 이미지를 새로 추가할 때, 5장을 초과하면 예외가 발생한다.")
    @Test
    void update_Board_With_Category_Is_Vote_When_Over_Five_Images() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when & then
        List<String> updateImages = createImagePaths(6);
        assertThatThrownBy(() -> board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                updateImages
        ))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 수정할 때, 이미지 1장을 삭제한다.")
    @Test
    void update_Board_With_Category_Is_Vote_Out_Images() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when
        List<String> updateImages = createImagePaths(2);
        board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                updateImages
        );

        // then
        assertThat(board.getTitle()).isEqualTo("update-title");
        assertThat(board.getBoardImages()).hasSize(2)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "s3-image-path-1",
                        "s3-image-path-2"
                );
    }

    @DisplayName("투표 타입의 게시글을 수정하여 이미지를 삭제했을 때, 2장 미만이 되면 예외가 발생한다.")
    @Test
    void update_Board_With_Category_Is_Vote_Out_Two_Images() {
        // given
        Member NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(NORMAL_MEMBER, images, VOTE);
        ReflectionTestUtils.setField(NORMAL_MEMBER, "memberId", 1L);

        // when & then
        List<String> updateImages = createImagePaths(1);
        assertThatThrownBy(() -> board.update(
                NORMAL_MEMBER.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                updateImages
        ))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("게시글을 수정할 때, 다른 사용자가 게시글을 수정할 수 없다.")
    @Test
    void update_Board_Only_Same_Writer() {
        // given
        Member MEMBER_LANGO = NORMAL_MEMBER_LANGO.createMember();
        Member MEMBER_SONNY = NORMAL_MEMBER_LANGO.createMember();
        List<String> images = createImagePaths(3);
        Board board = createBoard(MEMBER_LANGO, images, VOTE);
        ReflectionTestUtils.setField(MEMBER_LANGO, "memberId", 1L);
        ReflectionTestUtils.setField(MEMBER_SONNY, "memberId", 2L);

        // when & then
        assertThatThrownBy(() -> board.update(
                MEMBER_SONNY.getId(),
                "update-title",
                "update-content",
                VOTE,
                LocalDateTime.now(),
                images
        ))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD.getMessage());
    }

    private static Board createBoard(
            Member NORMAL_MEMBER,
            List<String> images,
            CategoryBoard type
    ) {
        return Board.builder()
                .title("title")
                .content("content")
                .member(NORMAL_MEMBER)
                .categoryBoard(type)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();
    }

    private static List<String> createImagePaths(
            int size
    ) {
        return IntStream.range(0, size)
                .mapToObj(i -> "s3-image-path-" + (i+1))
                .collect(Collectors.toList());
    }

}
