package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    //한명의 Member는 여러개의 게시글을 작성할 수 있다.(Many = Board, One = Member)
    //관계 적용 필요
    private Long memberId;
    @Column(length = 200, nullable = false)
    private String title;
    @Column(length = 100,nullable = false)
    private String content;
    private long like;
    private LocalDateTime deadLine;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CategoryBoard> cateSet = new HashSet<>();
    public void addCate(CategoryBoard categoryBoard) { this.cateSet.add(categoryBoard); }
}
