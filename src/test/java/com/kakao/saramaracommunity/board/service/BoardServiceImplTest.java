package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.board.service.dto.request.BoardServiceRequest;
import com.kakao.saramaracommunity.board.service.dto.response.BoardResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static org.assertj.core.api.Assertions.assertThat;

class BoardServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    @Autowired
    private BoardService boardService;

    @AfterEach
    void tearDown() {
        boardImageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 이미지 3장을 업로드하여 게시글을 생성한다.")
    @Test
    void createBoard() {
        // given
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(COMMENT_WRITER_SONNY);

        List<String> images = List.of("path1", "path2", "path3");

        BoardServiceRequest.BoardCreateServiceRequest request = BoardServiceRequest.BoardCreateServiceRequest.builder()
                .title("xxx")
                .content("xxx")
                .categoryBoard(CategoryBoard.VOTE)
                .memberId(savedMember.getMemberId())
                .deadLine(LocalDateTime.now())
                .boardImages(images)
                .build();

        // when
        BoardResponse.BoardCreateResponse result = boardService.createBoard(request);

        // then
        assertThat(result.getTitle()).isEqualTo("xxx");
        assertThat(result.getContent()).isEqualTo("xxx");
        assertThat(result.getCategoryBoard()).isEqualTo(CategoryBoard.VOTE);
    }

    @DisplayName("게시글의 제목과 내용, 이미지 한장을 새로 추가한다. - 트랜잭션 필요")
    @Transactional
    @Test
    void updateBoard() {
        // given
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(COMMENT_WRITER_SONNY);
        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("xxx")
                .content("xxx")
                .member(savedMember)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();
        Board savedBoard = boardRepository.save(board);

        List<String> updateImages = List.of(
                "path2",
                "path3",
                "new-path"
        );

        BoardServiceRequest.BoardUpdateServiceRequest request = BoardServiceRequest.BoardUpdateServiceRequest.builder()
                .memberId(COMMENT_WRITER_SONNY.getMemberId())
                .title("update-xxx")
                .content("update-xxx")
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .boardImages(updateImages)
                .build();

        // when
        boardService.updateBoard(savedBoard.getId(), request);

        // then
        // Board를 가져온 후 getBoardImages()로 연관관계 조회를 위해 새 트랜잭션이 필요하다.
        Board updatedBoard = boardRepository.findById(savedBoard.getId()).get();

        assertThat(updatedBoard.getTitle()).isEqualTo("update-xxx");
        assertThat(updatedBoard.getContent()).isEqualTo("update-xxx");
        assertThat(updatedBoard.getBoardImages()).hasSize(3)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path2", "path3", "new-path"
                );
    }

    @DisplayName("게시글의 이미지 중 한장을 수정한다. - 트랜잭션 필요 X")
    @Test
    void updateBoardFindBoardImage() {
        // given
        Member COMMENT_WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(COMMENT_WRITER_SONNY);

        List<String> images = List.of("path1", "path2", "path3");

        Board board = Board.builder()
                .title("xxx")
                .content("xxx")
                .member(savedMember)
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();
        Board savedBoard = boardRepository.save(board);

        List<String> updateImages = List.of(
                "path2",
                "path3",
                "new-path"
        );

        BoardServiceRequest.BoardUpdateServiceRequest request = BoardServiceRequest.BoardUpdateServiceRequest.builder()
                .memberId(COMMENT_WRITER_SONNY.getMemberId())
                .title("update-xxx")
                .content("update-xxx")
                .categoryBoard(CategoryBoard.VOTE)
                .deadLine(LocalDateTime.now())
                .boardImages(updateImages)
                .build();

        // when
        boardService.updateBoard(savedBoard.getId(), request);

        // then
        List<BoardImage> boardImages = boardImageRepository.findByBoardId(savedBoard.getId());
        assertThat(boardImages).hasSize(3)
                .extracting("path")
                .containsExactlyInAnyOrder(
                        "path2", "path3", "new-path"
                );
    }

}