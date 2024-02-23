package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.CHOICE;
import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.*;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;

@Getter
@Entity
@Table(name = "BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryBoard categoryBoard;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime deadLine;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<BoardImage> boardImages = new ArrayList<>();

    @Builder
    private Board(
            final Member member,
            final CategoryBoard categoryBoard,
            final String title,
            final String content,
            final LocalDateTime deadLine,
            final List<String> images
    ) {
        this.member = member;
        this.categoryBoard = categoryBoard;
        validateImageCount(images.size());
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.boardImages = createBoardImages(images);

    }

    public void update(
            final Long memberId,
            final String title,
            final String content,
            final CategoryBoard categoryBoard,
            final LocalDateTime deadLine,
            final List<String> images
    ) {
        validateWriter(this.member.getId(), memberId);
        checkCategory(categoryBoard);
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        validateImageCount(images.size());
        this.deadLine = deadLine;
        updateBoardImages(images);

    }

    private List<BoardImage> createBoardImages(List<String> requestCreateImages) {
        return requestCreateImages.stream()
                .map(image -> BoardImage.of(this, image))
                .collect(Collectors.toList());
    }

    private void updateBoardImages(List<String> reqeustUpdateImages) {
        Map<String, BoardImage> currentImagesMap = getNowImageMap();
        removeImages(reqeustUpdateImages);
        addImages(reqeustUpdateImages, currentImagesMap);
    }

    private Map<String, BoardImage> getNowImageMap() {
        return boardImages.stream()
                .collect(Collectors.toMap(BoardImage::getPath, Function.identity()));
    }

    private void removeImages(List<String> images) {
        boardImages.removeIf(image -> !images.contains(image.getPath()));
    }

    private void addImages(List<String> images, Map<String, BoardImage> imageMap) {
        images.stream()
                .filter(newImagePath -> !imageMap.containsKey(newImagePath))
                .map(newImagePath -> BoardImage.of(this, newImagePath))
                .forEach(boardImages::add);
    }

    private void validateWriter(Long originalWriter, Long requestWriter) {
        if (!originalWriter.equals(requestWriter)) {
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

    private void checkCategory(CategoryBoard requestCategoryBoard) {
        if(!categoryBoard.equals(requestCategoryBoard)) {
            throw new BoardBusinessException(BOARD_CATEGORY_MISMATCH);
        }
    }

    private void validateImageCount(int imageCount) {
        if (categoryBoard.equals(VOTE) && imageCount < 2) {
            throw new BoardBusinessException(BOARD_VOTE_IMAGE_RANGE_OUT);
        }
        if (categoryBoard.equals(CHOICE) && imageCount != 1) {
            throw new BoardBusinessException(BOARD_CHOICE_IMAGE_RANGE_OUT);
        }
    }

}
