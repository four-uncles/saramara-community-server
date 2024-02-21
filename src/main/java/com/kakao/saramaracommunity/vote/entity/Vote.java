package com.kakao.saramaracommunity.vote.entity;

import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
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
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "board_image_id")
    private BoardImage boardImage;


    @Builder(access = AccessLevel.PRIVATE)
    public Vote(Member member, Board board, BoardImage boardImage) {
        this.member = member;
        this.board = board;
        this.boardImage = boardImage;
    }

    public static Vote of(Member member, Board board, BoardImage boardImage) {
        return Vote.builder()
                .member(member)
                .board(board)
                .boardImage(boardImage)
                .build();
    }

    public void changeVote(Long requestMemberId, BoardImage boardImage) {
        validateVoter(member.getId(), requestMemberId);
        this.boardImage = boardImage;

    }

    /**
     * 투표 수정시 투표자를 검증하기 위한 메서드입니다.
     */
    private void validateVoter(Long originalVoter, Long requestVoter) {
        if(!originalVoter.equals(requestVoter)){
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

}
