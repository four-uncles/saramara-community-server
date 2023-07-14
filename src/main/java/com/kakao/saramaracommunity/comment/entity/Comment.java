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
@ToString(exclude = {"member", "board"})
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // 한 명의 유저가 여러 개의 코멘트 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;

    // 한 개의 Board에서 여러 개의 코멘트 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardId")
    private Board board;

    @Lob
    @Column(nullable = false)
    private String content;

    private Long pick;


    /**
     * 댓글 수정을 위한 메서드입니다.
     * 전체 수정이 아닌 부분 수정을 위해 내용과 픽만 변경시킬 수 있게 잡아놓았습니다.
     *
     * @param content
     * @param pick
     */
    public void changeComment(String content, Long pick) {
        this.content = content;
        this.pick = pick;
    }
}
