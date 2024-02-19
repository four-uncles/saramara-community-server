package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardDeleteServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardUpdateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardSearchResponse;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.SortType;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardSearchResponse searchBoards(Long cursorId, Pageable page, SortType sort) {
        List<Board> boards = getBoards(cursorId, page, sort);
        Long nextCursorId = getNextCursorId(sort, boards);
        Boolean hasNext = boards.size() >= page.getPageSize();
        return BoardSearchResponse.of(
                boards.stream()
                        .map(BoardGetResponse::of)
                        .collect(Collectors.toList()),
                hasNext,
                nextCursorId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BoardGetResponse getBoard(Long boardId) {
        Board board = getBoardEntity(boardId);
        return BoardGetResponse.of(board);
    }

    @Override
    public BoardCreateResponse createBoard(BoardCreateServiceRequest request) {
        Board createdBoard = boardRepository.save(
                request.toEntity(
                        getMemberEntity(request.memberId())
                )
        );
        log.info("[BoardServiceImpl] 게시글을 등록합니다. 게시글 번호: {}", createdBoard.getId());
        return BoardCreateResponse.of(createdBoard);
    }

    @Override
    public void updateBoard(Long boardId, BoardUpdateServiceRequest request) {
        Board savedBoard = getBoardEntity(boardId);
        log.info("[BoardServiceImpl] 게시글을 수정합니다. 게시글 번호: {}", savedBoard.getId());
        verifyBoardOwner(savedBoard, request.memberId());
        savedBoard.update(
                request.memberId(),
                request.title(),
                request.content(),
                request.categoryBoard(),
                request.deadLine(),
                request.boardImages()
        );
    }

    @Override
    public void deleteBoard(Long boardId, BoardDeleteServiceRequest request) {
        Board savedBoard = getBoardEntity(boardId);
        log.info("[BoardServiceImpl] 게시글을 삭제합니다. 게시글 번호: {}", savedBoard.getId());
        verifyBoardOwner(savedBoard, request.memberId());
        boardRepository.delete(savedBoard);
    }

    private void verifyBoardOwner(Board board, Long memberId) {
        if (!board.getMember().getId().equals(memberId)) throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
    }

    private Member getMemberEntity(Long memberId) {
        return Optional.ofNullable(memberId)
                .flatMap(memberRepository::findById)
                .orElseThrow(() -> new MemberBusinessException(MEMBER_NOT_FOUND));
    }

    private Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardBusinessException(BOARD_NOT_FOUND));
    }

    /**
     * @param sort 현재는 최신순 정렬(SortType - LATEST(DEFAULT))만 사용되어 파라미터의 sort는 사용되지 않음.
     */
    private Long getNextCursorId(SortType sort, List<Board> boards) {
        return boards.isEmpty() ? null : boards.get(boards.size() - 1).getId();
    }

    /**
     * @param sort 현재는 최신순 정렬(SortType - LATEST(DEFAULT))만 사용되어 파라미터의 sort는 사용되지 않음.
     */
    private List<Board> getBoards(Long cursorId, Pageable page, SortType sort) {
        log.info("[BoardServiceImpl] 최신순으로 게시글을 조회합니다.(Reading all boards by latest)");
        return cursorId == null ?
                boardRepository.findAllByOrderByCreatedAtDesc(page) :
                boardRepository.findByIdLessThanOrderByCreatedAtDesc(cursorId, page);
    }

}
