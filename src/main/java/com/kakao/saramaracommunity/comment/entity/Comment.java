package com.kakao.saramaracommunity.comment.entity;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private int pick;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Builder
    private Comment(Member member, Board board, String content, int pick, String attachmentUrl) {
        this.member = member;
        this.board = board;
        this.content = content;
        this.pick = pick;
        this.attachmentUrl = attachmentUrl;
    }

    public void changeComment(String content, String attachmentUrl) {
        this.content = content;
        this.attachmentUrl = attachmentUrl;
    }

    public void changeComment(String content, int pick, String attachmentUrl) {
        this.content = content;
        this.pick = pick;
        this.attachmentUrl = attachmentUrl;
    }

}
