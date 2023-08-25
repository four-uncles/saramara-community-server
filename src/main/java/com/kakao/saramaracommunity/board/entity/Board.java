package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update board set deleted_at = CURRENT_TIMESTAMP where board_id = ?")
@ToString(exclude = {"member", "categoryBoard"})
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId; // 게시글 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member; // 게시글 작성자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryBoard categoryBoard; // 카테고리 (VOTE, CHOICE ... DEFAULT: VOTE)

    @Column(length = 100, nullable = false)
    private String title; // 게시글 제목

    @Lob
    private String content; // 게시글 내용

    @Column(columnDefinition = "integer default 0", nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
    private Long boardCnt; // 게시글 조회수

    @Column(columnDefinition = "integer default 0", nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
    private Long likeCnt; // 게시글 좋아요 수

    private LocalDateTime deadLine;

    @Builder
    public Board(
        Long boardId, Member member, CategoryBoard categoryBoard, String title,
        String content, Long boardCnt, Long likeCnt, LocalDateTime deadLine) {

        this.boardId = boardId;
        this.member = member;
        this.categoryBoard = categoryBoard;
        this.title = title;
        this.content = content;
        this.boardCnt = boardCnt;
        this.likeCnt = likeCnt;
        this.deadLine = deadLine;
    }

    @PrePersist
    public void prePersist() {

        this.boardCnt = this.boardCnt == null ? 0 : this.boardCnt;
        this.likeCnt = this.likeCnt == null ? 0 : this.likeCnt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategoryBoard(CategoryBoard categoryBoard) {
        this.categoryBoard = categoryBoard;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public void updateBoard(String title, String content, CategoryBoard categoryBoard, LocalDateTime deadLine) {
        this.title = title;
        this.content = content;
        this.categoryBoard = categoryBoard;
        this.deadLine = deadLine;
    }
}