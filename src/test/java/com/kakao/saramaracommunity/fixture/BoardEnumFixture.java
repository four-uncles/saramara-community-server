package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.*;
import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberEnumFixture.NORMAL_MEMBER_SONNY;

public enum BoardEnumFixture {


    BOARD_VOTE_WRITER_LANGO(
            "집에서 입을 잠옷 어떤 것이 좋을까요?",
            "잠옷 후보 3개 정도를 추려봤는데 골라주세요!",
            NORMAL_MEMBER_LANGO.createMember(),
            VOTE,
            LocalDateTime.now(),
            List.of("image-1", "image-2", "image-3")
    ),
    BOARD_CHOICE_WRITER_SONNY(
            "겨울 다 지났는데 발마칸 코트를 살까요? 말까요?",
            "겨울 내내 고민이네요. 여러분들의 귀한 조언을 얻고 싶습니다.",
            NORMAL_MEMBER_SONNY.createMember(),
            CHOICE,
            LocalDateTime.now(),
            List.of("image-1")
    ),
    BOARD_NORMAL_WRITER_LANGO(
            "새해 기념으로 캠핑 용품을 구매했어요.",
            "XX 제품 정말 좋네요. 여러분들도 꼭 한 번쯤 사용해보셨음 합니다.",
            NORMAL_MEMBER_LANGO.createMember(),
            NORMAL,
            LocalDateTime.now(),
            List.of("image-1", "image-2")
    ),
    ;

    private final String title;
    private final String content;
    private final Member writer;
    private final CategoryBoard category;
    private final LocalDateTime deadLine;
    private final List<String> boardImages;

    BoardEnumFixture(
            String title,
            String content,
            Member writer,
            CategoryBoard category,
            LocalDateTime deadLine,
            List<String> boardImages
    ) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.category = category;
        this.deadLine = deadLine;
        this.boardImages = boardImages;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Member getWriter() {
        return writer;
    }

    public CategoryBoard getCategory() {
        return category;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public List<String> getBoardImages() {
        return boardImages;
    }

    public Board createBoard() {
        return Board.builder()
                .title(getTitle())
                .content(getContent())
                .member(getWriter())
                .categoryBoard(getCategory())
                .deadLine(getDeadLine())
                .attachPaths(getBoardImages())
                .build();
    }

}
