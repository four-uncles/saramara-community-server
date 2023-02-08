package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@DynamicInsert
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update board set deleted_at = CURRENT_TIMESTAMP where board_id = ?")
@ToString(exclude = {"member", "category"})
@Entity
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardId")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryBoard category;
    @Column(length = 100, nullable = false)
    private String title;
    @Lob
    private String content;
    @ColumnDefault("0")
    private Long likes;
    @ColumnDefault("0")
    private Long cnt;
    private LocalDateTime deadLine;

    @Builder
    public Board(Member member, CategoryBoard category, String title, String content, Long likes, LocalDateTime deadLine) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.deadLine = deadLine;
    }
}