package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.CHOICE;
import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.*;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

/**
 * 게시글(Board) 관련 도메인의 단위 테스트를 진행하는 테스트 클래스입니다.
 */
class BoardTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 게시글_생성_시 {
        private Member NORMAL_MEMBER;
        @BeforeEach
        void setUp() {
            NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
        }
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 투표_카테고리일_경우 {
            @Test
            @DisplayName("이미지는 최소 2장 이상 등록해야 한다.")
            void 이미지는_최소_2장_이상_등록해야_한다() {
                // given
                List<String> images = createImagePaths(3);

                // when
                Board result = createBoard(NORMAL_MEMBER, images, VOTE);

                // then
                assertThat(result.getBoardImages()).hasSize(3);
            }
            @Test
            @DisplayName("등록한 이미지가 1장일 경우 예외가 발생한다.")
            void 등록한_이미지가_1장일_경우_예외가_발생한다() {
                // given
                List<String> images = createImagePaths(1);

                // when & then
                assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, VOTE))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
            @Test
            @DisplayName("등록한 이미지가 5장을 초과할 경우 예외가 발생한다.")
            void 등록한_이미지가_5장을_초과할_경우_예외가_발생한다() {
                // given
                List<String> images = createImagePaths(6);

                // when & then
                assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, VOTE))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
            }
        }
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 찬반_카테고리일_경우 {
            @Test
            @DisplayName("이미지는 1장만 등록할 수 있다.")
            void 이미지는_1장만_등록할_수_있다() {
                // given
                List<String> images = createImagePaths(1);

                // when
                Board result = createBoard(NORMAL_MEMBER, images, CHOICE);

                // then
                assertThat(result.getBoardImages()).hasSize(1);
            }
            @Test
            @DisplayName("등록한 이미지가 1장을 초과할 경우 예외가 발생한다.")
            void 등록한_이미지가_1장을_초과할_경우_예외가_발생한다() {
                // given
                List<String> images = createImagePaths(2);

                // when & then
                assertThatThrownBy(() -> createBoard(NORMAL_MEMBER, images, CHOICE))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 게시글_수정_시 {
        private Member NORMAL_MEMBER;
        @BeforeEach
        void setUp() {
            NORMAL_MEMBER = NORMAL_MEMBER_LANGO.createMember();
            setField(NORMAL_MEMBER, "id", 1L);
        }
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 작성자_라면 {
            @Test
            @DisplayName("글 제목을 변경할 수 있다.")
            void 글_제목을_변경할_수_있다() {
                // given
                List<String> images = createImagePaths(3);
                Board board = createBoard(NORMAL_MEMBER, images, VOTE);

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
            }
            @Test
            @DisplayName("글 내용을 변경할 수 있다.")
            void 글_내용을_변경할_수_있다() {
                // given
                List<String> images = createImagePaths(1);
                Board board = createBoard(NORMAL_MEMBER, images, CHOICE);

                // when
                List<String> updateImages = createImagePaths(1);
                board.update(
                        NORMAL_MEMBER.getId(),
                        "update-title",
                        "update-content",
                        CHOICE,
                        LocalDateTime.now(),
                        updateImages
                );

                // then
                assertThat(board.getContent()).isEqualTo("update-content");
            }
            @Test
            @DisplayName("카테고리는 변경할 수 없다.")
            void 카테고리를_변경할_수_없다() {
                // given
                List<String> images = createImagePaths(1);
                Board board = createBoard(NORMAL_MEMBER, images, CHOICE);

                // when
                List<String> updateImages = createImagePaths(1);
                assertThatThrownBy(() ->
                        board.update(
                                NORMAL_MEMBER.getId(),
                                "update-title",
                                "update-content",
                                VOTE,
                                LocalDateTime.now(),
                                updateImages
                        ))
                        .isInstanceOf(BoardBusinessException.class)
                        .hasMessage(BOARD_CATEGORY_MISMATCH.getMessage());
            }

            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 투표_카테고리의_글을_수정할_때 {
                @Test
                @DisplayName("이미지는 최대 5장까지 추가할 수 있다.")
                void 이미지는_최대_5장까지_추가할_수_있다() {
                    // given
                    List<String> images = createImagePaths(3);
                    Board board = createBoard(NORMAL_MEMBER, images, VOTE);

                    // when
                    List<String> updateImages = createImagePaths(5);
                    board.update(
                            NORMAL_MEMBER.getId(),
                            "title",
                            "content",
                            VOTE,
                            LocalDateTime.now(),
                            updateImages
                    );

                    // then
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
                @Test
                @DisplayName("추가한 이미지가 5장을 초과할 경우 예외가 발생한다.")
                void 추가한_이미지가_5장을_초과할_경우_예외가_발생한다() {
                    // given
                    List<String> images = createImagePaths(3);
                    Board board = createBoard(NORMAL_MEMBER, images, VOTE);

                    // when & then
                    List<String> updateImages = createImagePaths(6);
                    assertThatThrownBy(() ->
                            board.update(
                                    NORMAL_MEMBER.getId(),
                                    "title",
                                    "content",
                                    VOTE,
                                    LocalDateTime.now(),
                                    updateImages
                            ))
                            .isInstanceOf(BoardBusinessException.class)
                            .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
                }
                @Test
                @DisplayName("이미지는 2장까지만 삭제할 수 있다.")
                void 이미지는_2장까지만_삭제할_수_있다() {
                    // given
                    List<String> images = createImagePaths(3);
                    Board board = createBoard(NORMAL_MEMBER, images, VOTE);

                    // when
                    List<String> updateImages = createImagePaths(2);
                    board.update(
                            NORMAL_MEMBER.getId(),
                            "title",
                            "content",
                            VOTE,
                            LocalDateTime.now(),
                            updateImages
                    );

                    // then
                    assertThat(board.getBoardImages()).hasSize(2)
                            .extracting("path")
                            .containsExactlyInAnyOrder(
                                    "s3-image-path-1",
                                    "s3-image-path-2"
                            );
                }
                @Test
                @DisplayName("이미지를 2장 미만으로 삭제할 경우 예외가 발생한다.")
                void 이미지를_2장_미만으로_삭제할_경우_예외가_발생한다() {
                    // given
                    List<String> images = createImagePaths(3);
                    Board board = createBoard(NORMAL_MEMBER, images, VOTE);

                    // when & then
                    List<String> updateImages = createImagePaths(1);
                    assertThatThrownBy(() ->
                            board.update(
                                    NORMAL_MEMBER.getId(),
                                    "title",
                                    "content",
                                    VOTE,
                                    LocalDateTime.now(),
                                    updateImages
                            ))
                            .isInstanceOf(BoardBusinessException.class)
                            .hasMessage(BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
                }
                @Test
                @DisplayName("기존에 등록했던 이미지는 그대로 유지된다.")
                void 기존에_등록한_이미지_정보는_유지된다() {
                    // given
                    List<String> images = createImagePaths(3);
                    Board board = createBoard(NORMAL_MEMBER, images, VOTE);

                    // when
                    List<String> updateImages = List.of(
                            "s3-image-path-4",
                            "s3-image-path-2", // 해당 Path는 이미 등록된 이미지
                            "s3-image-path-5"
                    );
                    board.update(
                            NORMAL_MEMBER.getId(),
                            "title",
                            "content",
                            VOTE,
                            LocalDateTime.now(),
                            updateImages
                    );

                    // then
                    assertThat(board.getBoardImages()).hasSize(3)
                            .extracting("path")
                            .containsExactlyInAnyOrder(
                                    "s3-image-path-2",
                                    "s3-image-path-4",
                                    "s3-image-path-5"
                            );
                }
            }
            @Nested
            @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
            class 찬반_카테고리의_글을_수정할_때 {
                @Test
                @DisplayName("등록한 이미지 1장만 변경할 수 있다.")
                void 등록한_이미지_1장만_변경할_수_있다() {
                    // given
                    List<String> images = createImagePaths(1);
                    Board board = createBoard(NORMAL_MEMBER, images, CHOICE);

                    // when
                    List<String> updateImages = createImagePaths(1);
                    board.update(
                            NORMAL_MEMBER.getId(),
                            "title",
                            "content",
                            board.getCategoryBoard(),
                            LocalDateTime.now(),
                            updateImages
                    );

                    // then
                    assertThat(board.getBoardImages()).hasSize(1)
                            .extracting("path")
                            .containsExactlyInAnyOrder(
                                    "s3-image-path-1"
                            );
                }
                @Test
                @DisplayName("추가한 이미지가 1장을 초과할 경우 예외가 발생한다.")
                void 추가한_이미지가_1장을_초과할_경우_예외가_발생한다() {
                    // given
                    List<String> images = createImagePaths(1);
                    Board board = createBoard(NORMAL_MEMBER, images, CHOICE);

                    // when & then
                    List<String> updateImages = createImagePaths(3);
                    assertThatThrownBy(() ->
                            board.update(
                                    NORMAL_MEMBER.getId(),
                                    "title",
                                    "content",
                                    board.getCategoryBoard(),
                                    LocalDateTime.now(),
                                    updateImages
                            ))
                            .isInstanceOf(BoardBusinessException.class)
                            .hasMessage(BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
                }
            }
        }
        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 작성자가_아니라면 {
            @Test
            @DisplayName("수정할 수 없다.")
            void 수정할_수_없다() {
                // given
                Member NORMAL_MEMBER_NOT_WRITER = NORMAL_MEMBER_LANGO.createMember();
                List<String> images = createImagePaths(3);
                Board board = createBoard(NORMAL_MEMBER, images, VOTE);
                setField(NORMAL_MEMBER_NOT_WRITER, "id", 2L);

                // when & then
                assertThatThrownBy(() ->
                        board.update(
                                NORMAL_MEMBER_NOT_WRITER.getId(),
                                "update-title",
                                "update-content",
                                VOTE,
                                LocalDateTime.now(),
                                images
                        ))
                        .isInstanceOf(MemberBusinessException.class)
                        .hasMessage(UNAUTHORIZED_TO_MEMBER.getMessage());
            }
        }

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
