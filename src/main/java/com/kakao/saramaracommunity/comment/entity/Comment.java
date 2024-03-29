package com.kakao.saramaracommunity.comment.entity;

import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(Member member, Board board, String content) {
        this.member = member;
        this.board = board;
        this.content = content;
    }

    public static Comment of(Member member, Board board, String content) {
        return Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
    }

    public void changeComment(Long requestMemberId, String content) {
        validateWriter(member.getId(), requestMemberId);
        this.content = content;
    }

    /**
     * 댓글 수정시 작성자를 검증하기 위한 메서드입니다.
     */
    private void validateWriter(Long originalWriter, Long requestWriter) {
        if(!originalWriter.equals(requestWriter)){
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

}
