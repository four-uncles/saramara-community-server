package com.kakao.saramaracommunity.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.exception.BoardErrorCode;
import com.kakao.saramaracommunity.board.exception.BoardInternalServerException;
import com.kakao.saramaracommunity.board.exception.BoardNotFoundException;
import com.kakao.saramaracommunity.board.exception.BoardUnauthorizedException;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.board.service.dto.request.BoardServiceRequestDto;
import com.kakao.saramaracommunity.board.service.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.util.CursorResult;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Board saveBoard(BoardServiceRequestDto.SaveRequestDto requestDto) {

        String deadLineStr = String.valueOf(requestDto.getDeadLine()); // 문자열 값으로 받음
        LocalDateTime deadLine = LocalDateTime.parse(deadLineStr); // 문자열을 LocalDateTime으로 변환

        // 요청된 데이터로 Member 객체를 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD));

        // Board Entity 생성
        Board board = Board.builder()
            .member(member)
            .categoryBoard(requestDto.getCategoryBoard())
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .deadLine(deadLine)
            .build();

        return boardRepository.save(board);
    }

    @Override
    public BoardResponseDto.ReadOneBoardResponseDto readOneBoard(Long boardId) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        // Member information
        String memberNickname = board.getMember().getNickname();
        String memberEmail = board.getMember().getEmail();

        // 게시글 정보와 멤버 정보를 매핑해서 응답
        return BoardResponseDto.ReadOneBoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryBoard(board.getCategoryBoard())
                .memberNickname(memberNickname)
                .memberEmail(memberEmail)
                .boardCnt(board.getBoardCnt())
                .likeCnt(board.getBoardCnt())
                .deadLine(board.getDeadLine())
                .build();
    }

    @Override
    public CursorResult<BoardResponseDto.ReadAllBoardResponseDto> readAllBoardsByLatest(Long boardId, Pageable page) {
        List<Board> boards = boardId == null ?
            boardRepository.findAllByOrderByCreatedAtDesc(page) :
            boardRepository.findByBoardIdLessThanOrderByCreatedAtDesc(boardId, page);

        log.info("최신순으로 게시글을 조회합니다.(Reading all boards by latest)");

        Long nextCursorId = boards.isEmpty() ? null : boards.get(boards.size() - 1).getBoardId();
        Boolean hasNext = boards.size() >= page.getPageSize();

        List<BoardResponseDto.ReadAllBoardResponseDto> toServiceResBoardDto = boards.stream()
            .map(board -> BoardResponseDto.ReadAllBoardResponseDto.builder()
                .title(board.getTitle())
                .memberNickname(board.getMember().getNickname())
                .boardCnt(board.getBoardCnt())
                .likeCnt(board.getLikeCnt())
                .deadLine(board.getDeadLine())
                .build())
            .collect(Collectors.toList());

        return CursorResult.<BoardResponseDto.ReadAllBoardResponseDto>builder()
            .values(toServiceResBoardDto)
            .hasNext(hasNext)
            .nextCursorId(nextCursorId)
            .build();
    }

    @Override
    public CursorResult<BoardResponseDto.ReadAllBoardResponseDto> readAllBoardsByPopularity(Long likeCnt, Pageable page) {
        List<Board> boards = likeCnt == null ?
            boardRepository.findAllByOrderByLikeCntDesc(page) :
            boardRepository.findByLikeCntLessThanOrderByLikeCntDesc(likeCnt, page);

        log.info("인기순으로 게시글을 조회합니다.(Reading all boards by popularity)");

        Long nextCursorId = boards.isEmpty() ? null : boards.get(boards.size() - 1).getLikeCnt();
        Boolean hasNext = boards.size() >= page.getPageSize();

        List<BoardResponseDto.ReadAllBoardResponseDto> toServiceResBoardDto = boards.stream()
            .map(board -> BoardResponseDto.ReadAllBoardResponseDto.builder()
                .title(board.getTitle())
                .memberNickname(board.getMember().getNickname())
                .boardCnt(board.getBoardCnt())
                .likeCnt(board.getLikeCnt())
                .deadLine(board.getDeadLine())
                .build())
            .collect(Collectors.toList());

        return CursorResult.<BoardResponseDto.ReadAllBoardResponseDto>builder()
            .values(toServiceResBoardDto)
            .hasNext(hasNext)
            .nextCursorId(nextCursorId)
            .build();
    }

    @Override
    public Boolean updateBoard(Long boardId, BoardServiceRequestDto.UpdateRequestDto requestDto) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        if (!board.getMember().getMemberId().equals(requestDto.getMemberId())) {
            throw new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD);
        }

        log.info("요청에 따라 게시글을 수정합니다.(Update the post as requested.)");

        // 요청된 데이터로 수정할 내용 업데이트
        board.updateBoard(
            requestDto.getTitle(),
            requestDto.getContent(),
            requestDto.getCategoryBoard(),
            requestDto.getDeadLine());

        // 수정된 게시글을 저장
        boardRepository.save(board);
        return true;
    }

    @Override
    public Boolean deleteBoard(Long boardId) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        log.info("요청에 따라 게시글을 삭제합니다.(Delete the post as requested.)");

        boardRepository.deleteById(boardId);
        if (boardRepository.existsById(boardId)) {
            throw new BoardInternalServerException(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD);
        }
        return true;
    }
}