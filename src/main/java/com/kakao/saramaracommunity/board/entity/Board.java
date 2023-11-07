package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "categoryBoard"})
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<AttachBoard> attachBoards = new ArrayList<>();

    @Builder
    private Board(
            Member member,
            CategoryBoard categoryBoard,
            String title,
            String content,
            Long viewCount,
            Long likeCount,
            LocalDateTime deadLine,
            List<String> attachPaths
    ) {
        this.member = member;
        this.categoryBoard = categoryBoard;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.deadLine = deadLine;
        this.attachBoards = AttachBoard.createAttachBoards(this, attachPaths);
    }

    @PrePersist
    public void prePersist() {
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
    }

    public void update(
            String title,
            String content,
            CategoryBoard categoryBoard,
            LocalDateTime deadLine,
            List<String> attachPaths
    ) {
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        this.deadLine = deadLine;
        this.attachBoards = AttachBoard.updateAttachBoards(this, attachPaths);
    }

}