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
    private String content;

    @ColumnDefault("0")
    private Long likes;

    private Long pick;


   @Builder
    public Comment(Long commentId, Board board, Member member, String content, Long likes, Long pick) {
        this.commentId = commentId;
        this.board = board;
        this.member = member;
        this.content = content;
        this.likes = likes;
        this.pick = pick;
    }

    // 수정을 위한 메서드. 내용과 사진 픽 숫자를 바꿔주게 된다.
    public void change(String content, Long pick){
        this.content = content;
        this.pick = pick;
    }

    @PrePersist
    public void prePersist() {
        this.likes = this.likes == null ? 0 : this.likes;
    }
}
