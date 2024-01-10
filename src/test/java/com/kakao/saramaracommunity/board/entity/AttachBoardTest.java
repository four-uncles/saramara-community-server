package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AttachBoardTest {

    @DisplayName("게시글에 등록된 첨부목록 중 하나의 게시글 첨부정보 순서와 객체 URL을 수정한다.")
    @Test
    void updateAttachBoard() {
        // given
        List<String> attachPaths = List.of("image1.png", "image2.png");
        Board board = createBoard(createMember(), attachPaths);
        AttachBoard attachBoard1 = AttachBoard.builder()
                .board(board)
                .imageOrder(1)
                .imagePath(attachPaths.get(0))
                .build();

        // when
        attachBoard1.update(1, "image3.png");

        // then
        assertThat(attachBoard1.getImageOrder()).isEqualTo(1);
        assertThat(attachBoard1.getImagePath()).isEqualTo("image3.png");
    }

    private static Member createMember() {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password("test")
                // .type(Type.LOCAL)
                .build();
    }

    private static Board createBoard(Member member, List<String> attachPaths) {
        return Board.builder()
                .title("title")
                .content("content")
                .member(member)
                .categoryBoard(CategoryBoard.CHOICE)
                .deadLine(LocalDateTime.now())
                .attachPaths(attachPaths)
                .build();
    }
}