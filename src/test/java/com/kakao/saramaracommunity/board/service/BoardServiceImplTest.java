package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardUpdateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.entity.CategoryBoard;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.exception.BoardErrorCode;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.CHOICE;
import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_SONNY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 게시글(Board) 관련 비즈니스 계층의 통합 테스트를 위한 테스트 클래스입니다.
 */
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

    @DisplayName("투표 타입의 게시글을 생성할 경우, 이미지를 2장 이상 등록해야 한다.")
    @Test
    void create_Board_Category_Is_Vote() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        BoardCreateServiceRequest request = createSaveRequest(VOTE, savedMember, images);

        // when
        BoardCreateResponse result = boardService.createBoard(request);

        // then
        assertThat(result.title()).isEqualTo("xxx");
        assertThat(result.content()).isEqualTo("xxx");
        assertThat(result.categoryBoard()).isEqualTo(VOTE);
        assertThat(result.boardImages()).hasSize(3);
    }

    @DisplayName("투표 타입의 게시글을 생성할 때, 등록한 이미지가 1장일 경우 예외가 발생한다.")
    @Test
    void create_Board_Category_Is_Vote_When_One_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(1);
        BoardCreateServiceRequest request = createSaveRequest(VOTE, savedMember, images);

        // when & then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 생성할 때, 등록한 이미지가 5장을 초과할 경우 예외가 발생한다.")
    @Test
    void create_Board_Category_Is_Vote_When_Over_Five_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(6);
        BoardCreateServiceRequest request = createSaveRequest(VOTE, savedMember, images);

        // when & then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("찬반 타입의 게시글을 생성할 경우, 이미지를 1장만 등록할 수 있다.")
    @Test
    void create_Board_Category_Is_Choice() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(1);
        BoardCreateServiceRequest request = createSaveRequest(CHOICE, savedMember, images);

        // when
        BoardCreateResponse result = boardService.createBoard(request);

        // then
        assertThat(result.title()).isEqualTo("xxx");
        assertThat(result.content()).isEqualTo("xxx");
        assertThat(result.categoryBoard()).isEqualTo(CHOICE);
        assertThat(result.boardImages()).hasSize(1);
    }

    @DisplayName("찬반 타입의 게시글을 생성할 때, 등록한 이미지가 1장을 초과할 경우 예외가 발생한다.")
    @Test
    void create_Board_Category_Is_Choice_When_Two_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(2);
        BoardCreateServiceRequest request = createSaveRequest(CHOICE, savedMember, images);

        // when & then
        assertThatThrownBy(() -> boardService.createBoard(request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("게시글 작성자만이 게시글을 수정할 수 있다.")
    @Test
    void update_Board_Only_Writer_Allow() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(3);
        BoardUpdateServiceRequest request = createUpdateRequest(VOTE, WRITER_SONNY, updateImages);

        // when
        boardService.updateBoard(savedBoard.getId(), request);

        // then
        assertThat(board.getMember()).isSameAs(savedMember);
    }

    @DisplayName("게시글 작성자가 아닐 경우, 게시글을 수정할 수 없다.")
    @Test
    void update_Board_Other_Writer_Not_Allow() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member NOT_WRITER_LANGO = NORMAL_MEMBER_LANGO.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        memberRepository.save(NOT_WRITER_LANGO);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(3);
        BoardUpdateServiceRequest request = createUpdateRequest(VOTE, NOT_WRITER_LANGO, updateImages);

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(savedBoard.getId(), request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 수정할 경우, 이미지는 최대 5장까지 추가할 수 있다.")
    @Test
    void update_Board_Category_Is_Vote_Limit_Five_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(5);
        BoardUpdateServiceRequest request = createUpdateRequest(VOTE, WRITER_SONNY, updateImages);

        // when
        boardService.updateBoard(savedBoard.getId(), request);

        // then
        List<BoardImage> result = boardImageRepository.findByBoardId(board.getId());
        assertThat(result).hasSize(5);
    }

    @DisplayName("투표 타입의 게시글을 수정할 경우, 이미지가 5장을 초과한다면 예외가 발생한다.")
    @Test
    void update_Board_Category_Is_Vote_Over_Five_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(7);
        BoardUpdateServiceRequest request = createUpdateRequest(VOTE, WRITER_SONNY, updateImages);

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(savedBoard.getId(), request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("투표 타입의 게시글을 수정할 경우, 이미지를 2장 미만으로 삭제하면 예외가 발생한다.")
    @Test
    void update_Board_Category_Is_Vote_Under_Two_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(1);
        BoardUpdateServiceRequest request = createUpdateRequest(VOTE, WRITER_SONNY, updateImages);

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(savedBoard.getId(), request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_VOTE_IMAGE_RANGE_OUT.getMessage());
    }

    @DisplayName("찬반 타입의 게시글을 수정할 경우, 이미지가 1장이 아니라면 예외가 발생한다.")
    @Test
    void update_Board_Category_Is_Choice_Over_Five_Images() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(1);
        Board board = createBoard(CHOICE, savedMember, images);
        Board savedBoard = boardRepository.save(board);
        List<String> updateImages = createImagePaths(2);
        BoardUpdateServiceRequest request = createUpdateRequest(CHOICE, WRITER_SONNY, updateImages);

        // when & then
        assertThatThrownBy(() -> boardService.updateBoard(savedBoard.getId(), request))
                .isInstanceOf(BoardBusinessException.class)
                .hasMessage(BoardErrorCode.BOARD_CHOICE_IMAGE_RANGE_OUT.getMessage());
    }
    
    @DisplayName("게시글을 삭제할 경우, 등록된 게시글 이미지까지 모두 삭제된다.")
    @Test
    void delete_Board() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(1);
        Board board = createBoard(CHOICE, savedMember, images);
        Board savedBoard = boardRepository.save(board);

        // when
        boardService.deleteBoard(savedBoard.getId());

        // then
        Optional<Board> result = boardRepository.findById(savedBoard.getId());
        List<BoardImage> deletedImages = boardImageRepository.findByBoardId(savedBoard.getId());
        assertThat(result).isEmpty();
        assertThat(deletedImages).isEmpty();
    }

    @DisplayName("투표 타입의 게시글 상세정보를 조회한다.")
    @Test
    void get_Board_Category_Is_Vote() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(3);
        Board board = createBoard(VOTE, savedMember, images);
        Board savedBoard = boardRepository.save(board);

        // when
        BoardGetResponse result = boardService.getBoard(savedBoard.getId());

        // then
        assertThat(result.title()).isEqualTo("xxx");
        assertThat(result.content()).isEqualTo("xxx");
        assertThat(result.categoryBoard()).isEqualTo(VOTE);
        assertThat(result.boardImages()).hasSize(3);
    }

    @DisplayName("찬반 타입의 게시글 상세정보를 조회한다.")
    @Test
    void get_Board() {
        // given
        Member WRITER_SONNY = NORMAL_MEMBER_SONNY.createMember();
        Member savedMember = memberRepository.save(WRITER_SONNY);
        List<String> images = createImagePaths(1);
        Board board = createBoard(CHOICE, savedMember, images);
        Board savedBoard = boardRepository.save(board);

        // when
        BoardGetResponse result = boardService.getBoard(savedBoard.getId());

        // then
        assertThat(result.title()).isEqualTo("xxx");
        assertThat(result.content()).isEqualTo("xxx");
        assertThat(result.categoryBoard()).isEqualTo(CHOICE);
        assertThat(result.boardImages()).hasSize(1);
    }

    private static Board createBoard(
            CategoryBoard category,
            Member savedMember,
            List<String> images
    ) {
        return Board.builder()
                .title("xxx")
                .content("xxx")
                .member(savedMember)
                .categoryBoard(category)
                .deadLine(LocalDateTime.now())
                .images(images)
                .build();
    }

    private static List<String> createImagePaths(
            int size
    ) {
        return IntStream.range(0, size)
                .mapToObj(i -> "s3-image-path-" + (i+1))
                .collect(Collectors.toList());
    }

    private static BoardCreateServiceRequest createSaveRequest(
            CategoryBoard category,
            Member member,
            List<String> images
    ) {
        return BoardCreateServiceRequest.builder()
                .title("xxx")
                .content("xxx")
                .categoryBoard(category)
                .memberId(member.getId())
                .deadLine(LocalDateTime.now())
                .boardImages(images)
                .build();
    }

    private static BoardUpdateServiceRequest createUpdateRequest(
            CategoryBoard category,
            Member member,
            List<String> images
    ) {
        return BoardUpdateServiceRequest.builder()
                .memberId(member.getId())
                .title("update-xxx")
                .content("update-xxx")
                .categoryBoard(category)
                .deadLine(LocalDateTime.now())
                .boardImages(images)
                .build();
    }

}
