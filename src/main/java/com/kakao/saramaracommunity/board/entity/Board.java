package com.kakao.saramaracommunity.board.entity;

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

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long viewCount;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long likeCount;

    private LocalDateTime deadLine;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<BoardImage> boardImages = new ArrayList<>();

    @Builder
    private Board(
            Member member,
            CategoryBoard categoryBoard,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            LocalDateTime deadLine,
            List<String> images
    ) {
        this.member = member;
        this.categoryBoard = categoryBoard;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.deadLine = deadLine;
        this.boardImages = createBoardImages(images);
    }

    public void update(
            String title,
            String content,
            // member 정보를 받아 작성자 여부를 비교해야 함.
            CategoryBoard categoryBoard,
            LocalDateTime deadLine,
            List<String> images
    ) {
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        this.deadLine = deadLine;
        updateBoardImages(images);
    }

    private List<BoardImage> createBoardImages(List<String> images) {
        return images.stream()
                .map(image -> new BoardImage(this, image))
                .collect(Collectors.toList());
    }

    // 게시글 이미지 목록 내 이미지 존재 유무를 통해, 게시글 이미지 등록 및 삭제 처리를 진행합니다.
    private void updateBoardImages(List<String> images) {
        // 1. 기존 이미지를 Map 형태로 변환 (Key: path, Value: BoardImage)
        Map<String, BoardImage> imageMap = getNowImageMap();

        // 2. 기존 이미지가 수정 목록에 없다면, 해당 이미지는 제거한다.
        // orphanRemoval 설정으로 연관관계가 끊어진 BoardImage는 삭제 처리
        removeImages(images);

        // 3. 기존에 없던 이미지가 존재한다면, 새로운 이미지로 추가한다.
        addImages(images, imageMap);
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
                .map(newImagePath -> new BoardImage(this, newImagePath))
                .forEach(this.boardImages::add);
    }

    @PrePersist
    public void prePersist() {
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
    }

}