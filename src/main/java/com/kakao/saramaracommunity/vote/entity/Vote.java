package com.kakao.saramaracommunity.vote.entity;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Vote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_image_id")
    private BoardImage boardImage;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Vote(Member member, BoardImage boardImage, Board board) {
        this.member = member;
        this.boardImage = boardImage;
        this.board = board;
    }

}
