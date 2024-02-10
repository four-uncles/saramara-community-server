package com.kakao.saramaracommunity.fixture;

import static com.kakao.saramaracommunity.fixture.BoardFixture.BOARD_VOTE_WRITER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_WOOGI;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.member.entity.Member;

public enum CommentFixture {

    COMMENT_WRITER_LANGO(
            NORMAL_MEMBER_LANGO.createMember(),
            BOARD_VOTE_WRITER_LANGO.createBoard(),
            "게시글 작성자 입니다. 많은 투표 부탁해요!"
    ),
    COMMENT_WRITER_SONNY(
            NORMAL_MEMBER_SONNY.createMember(),
            BOARD_VOTE_WRITER_LANGO.createBoard(),
            "1번 잠옷 농협은행!"
    ),
    COMMENT_WRITER_WOOGI(
            NORMAL_MEMBER_WOOGI.createMember(),
            BOARD_VOTE_WRITER_LANGO.createBoard(),
            "3번 잠옷 농협은행!"
    ),
    ;

    private final Member writer;
    private final Board board;
    private final String content;

    CommentFixture(
            Member writer,
            Board board,
            String content
    ) {
        this.writer = writer;
        this.board = board;
        this.content = content;
    }

    public Member getWriter() {
        return writer;
    }

    public Board getBoard() {
        return board;
    }

    public String getContent() {
        return content;
    }

    public Comment createComment() {
        return Comment.of(getWriter(), getBoard(), getContent());
    }

}
