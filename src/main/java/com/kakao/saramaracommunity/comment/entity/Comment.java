package com.kakao.saramaracommunity.comment.entity;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update comment set deleted_at = CURRENT_TIMESTAMP where comment_id = ?")
@ToString(exclude = {"board", "member"})
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // 하나의 Board가 여러 개의 코멘트 달기 가능
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    // 한 명의 유저가 여러 개의 코멘트 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Lob
    @Column(nullable = false)
    private String text;

    @ColumnDefault("0")
    private Long likes;

    private Integer pick;

    @Builder
    public Comment(Board board, Member member, String text, Long likes, Integer pick) {
        this.board = board;
        this.member = member;
        this.text = text;
        this.likes = likes;
        this.pick = pick;
    }
}
