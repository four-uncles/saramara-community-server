package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.exception.BoardErrorCode;
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

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;

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
        validateImageCount(this.categoryBoard, images);
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
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        this.deadLine = deadLine;
        updateBoardImages(images);
        validateImageCount(this.categoryBoard, images);
    }

    private List<BoardImage> createBoardImages(List<String> requestCreateImages) {
        return requestCreateImages.stream()
                .map(image -> BoardImage.of(this, image))
                .collect(Collectors.toList());
    }

    /**
     * updateBoardImages
     * 게시글 이미지 목록 내 이미지 존재 유무를 통해, 게시글 이미지 등록 및 삭제 처리를 진행합니다.
     * 1. 기존 이미지를 Map 형태로 변환 (Key: path, Value: BoardImage)
     * 2. 수정 요청의 이미지 개수가 기존 이미지 개수보다 적다면, 기존 이미지에서 존재하지 않는 이미지 객체를 삭제한다. 이때, orphanRemoval 설정으로 연관관계가 끊어진 BoardImage는 삭제 처리한다.
     * 3. 수정 요청의 이미지 개수가 기존 이미지 개수보다 많다면, 이미지 목록에 새로운 BoardImage를 생성한 후 추가한다.
     */
    private void updateBoardImages(List<String> reqeustUpdateImages) {
        Map<String, BoardImage> imageMap = getNowImageMap();
        if (reqeustUpdateImages.size() < imageMap.size()) removeImages(reqeustUpdateImages);
        else addImages(reqeustUpdateImages, imageMap);
    }

    private Map<String, BoardImage> getNowImageMap() {
        return this.boardImages.stream()
                .collect(Collectors.toMap(BoardImage::getPath, Function.identity()));
    }

    private void removeImages(List<String> images) {
        this.boardImages.removeIf(image -> !images.contains(image.getPath()));
    }

    private void addImages(List<String> images, Map<String, BoardImage> imageMap) {
        images.stream()
                .filter(newImagePath -> !imageMap.containsKey(newImagePath))
                .map(newImagePath -> BoardImage.of(this, newImagePath))
                .forEach(this.boardImages::add);
    }

    /**
     * validateWriter
     * 게시글의 수정시 인가를 위한 검증 메서드입니다.
     */
    private void validateWriter(Long originalWriter, Long requestWriter) {
        if(!originalWriter.equals(requestWriter)) throw new BoardBusinessException(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD);
    }

    /**
     * validateImageCount
     * 게시글의 카테고리(CHOICE, VOTE)별 이미지 목록 검증
     * CHOICE: boardImages 리스트의 개수가 반드시 1개 이하여야 한다.
     * VOTE: boardImages 리스트의 개수가 반드시 2개 이상, 5개 이하이어야 한다.
     */
    private void validateImageCount(CategoryBoard type, List<String> images) {
        if (type.equals(VOTE)) {
            if (images.size() < 2 || images.size() > 5) {
                throw new BoardBusinessException(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT);
            }
        } else {
            if (images.size() != 1) {
                throw new BoardBusinessException(BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT);
            }
        }
    }

}
