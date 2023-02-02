package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update board set deleted_at = CURRENT_TIMESTAMP where board_id = ?")
@ToString(exclude = {"writer", "cateSet"})
@Entity
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    //한명의 Member는 여러개의 게시글을 작성할 수 있다.(Many = Board, One = Member)
    //관계 적용 필요
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 100,nullable = false)
    private String content;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long likes;
    private LocalDateTime deadLine;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Column(name = "cate", nullable = false)
    private Set<CategoryBoard> cateSet = new HashSet<>();
    public void addCate(CategoryBoard categoryBoard) { this.cateSet.add(categoryBoard); }


}