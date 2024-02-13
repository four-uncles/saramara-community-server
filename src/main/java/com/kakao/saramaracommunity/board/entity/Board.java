package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
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
            Member member,
            CategoryBoard categoryBoard,
            String title,
            String content,
            LocalDateTime deadLine,
            List<String> images
    ) {
        this.member = member;
        this.categoryBoard = categoryBoard;
        this.title = title;
        this.content = content;
        this.deadLine = deadLine;
        this.boardImages = createBoardImages(images);
        validateImageCount(images.size());
    }

    public void update(
            Long memberId,
            String title,
            String content,
            CategoryBoard categoryBoard,
            LocalDateTime deadLine,
            List<String> images
    ) {
        validateWriter(this.member.getId(), memberId);
        checkCategory(categoryBoard);
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        this.deadLine = deadLine;
        updateBoardImages(images);
        validateImageCount(images.size());
    }

    /**
     * 게시글 이미지(BoardImage) 엔티티를 등록하는 메서드입니다.
     */
    private List<BoardImage> createBoardImages(List<String> requestCreateImages) {
        return requestCreateImages.stream()
                .map(image -> BoardImage.of(this, image))
                .collect(Collectors.toList());
    }

    /**
     * 게시글 이미지 목록의 변경점을 반영하는 메서드입니다.
     * 1. 기존 이미지를 Map 형식으로 변환히야 가져옵니다. (Key: String Path, Value: BoardImage)
     * 2. 수정 요청으로 받은 이미지 목록과 기존 등록된 이미지 목록을 비교하여 존재하지 않는 이미지는 삭제합니다. 이때, orphanRemoval 설정으로 연관관계가 끊어진 BoardImage는 삭제 처리합니다.
     * 3. 수정 요청으로 받은 이미지 목록에서 새롭게 추가된 이미지가 있다면, 새로운 게시글 이미지(BoardImage)를 등록합니다.
     */
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

    /**
     * 게시글 수정 시 작성자 여부를 검증하는 메서드입니다.
     */
    private void validateWriter(Long originalWriter, Long requestWriter) {
        if (!originalWriter.equals(requestWriter)) {
            throw new BoardBusinessException(UNAUTHORIZED_TO_UPDATE_BOARD);
        }
    }

    /**
     * 게시글 수정 시 카테고리 변경 여부를 검증하는 메서드입니다.
     */
    private void checkCategory(CategoryBoard requestCategoryBoard) {
        if(!categoryBoard.equals(requestCategoryBoard)) {
            throw new BoardBusinessException(BOARD_CATEGORY_MISMATCH);
        }
    }

    /**
     * 게시글의 카테고리(CHOICE, VOTE)별 이미지 개수를 검증하는 메서드입니다.
     * CHOICE: boardImages 리스트의 개수가 반드시 1개 이하여야 합니다.
     * VOTE: boardImages 리스트의 개수가 반드시 2개 이상, 5개 이하여야 합니다.
     */
    private void validateImageCount(int imageListSize) {
        if (categoryBoard.equals(VOTE) && (imageListSize < 2 || imageListSize > 5)) {
            throw new BoardBusinessException(BOARD_VOTE_IMAGE_RANGE_OUT);
        }
        if (categoryBoard.equals(CHOICE) && imageListSize != 1) {
            throw new BoardBusinessException(BOARD_CHOICE_IMAGE_RANGE_OUT);
        }
    }

}
