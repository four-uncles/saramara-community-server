package com.kakao.saramaracommunity.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.exception.BoardErrorCode;
import com.kakao.saramaracommunity.board.exception.BoardInternalServerException;
import com.kakao.saramaracommunity.board.exception.BoardNotFoundException;
import com.kakao.saramaracommunity.board.exception.BoardUnauthorizedException;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Board saveBoard(BoardRequestDto.SaveRequestDto requestDto) {

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

        Board saveBoard = boardRepository.save(board);
        if (saveBoard == null) {
            throw new BoardInternalServerException(BoardErrorCode.BOARD_CREATE_FAILED);
        }
        return saveBoard;
    }

    @Override
    public BoardResponseDto.ReadOneBoardResponseDto readOneBoard(Long boardId) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        // Member information
        String memberNickname = board.getMember().getNickname();
        String memberEmail = board.getMember().getEmail();

        // 게시글 정보와 멤버 정보를 매핑해서 응답
        BoardResponseDto.ReadOneBoardResponseDto responseDto =
            BoardResponseDto.ReadOneBoardResponseDto.builder()
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

        return responseDto;
    }

    @Override
    public List<BoardResponseDto.ReadAllBoardResponseDto> readAllBoardsByLatest() {
        List<Board> boards = boardRepository.findAllOrderByCreatedAtDesc();

        log.info("최신순으로 게시글을 조회합니다.(Reading all boards by latest)");

        return mapBoardListToResponseDtoList(boards);
    }

    @Override
    public List<BoardResponseDto.ReadAllBoardResponseDto> readAllBoardsByPopularity() {
        List<Board> boards = boardRepository.findAllOrderByLikeCntDesc();

        log.info("인기순으로 게시글을 조회합니다.(Reading all boards by popularity)");

        return mapBoardListToResponseDtoList(boards);
    }

    private List<BoardResponseDto.ReadAllBoardResponseDto> mapBoardListToResponseDtoList(List<Board> boards) {

        log.info("게시글 목록을 응답 DTO 목록에 Mapping 합니다.: "
            + "(Mapping board list to response DTO list)");

        return boards.stream().map(board -> {
            String memberNickname = board.getMember().getNickname();
            Long boardCnt = board.getBoardCnt();
            Long likeCnt = board.getLikeCnt();
            LocalDateTime deadLine = board.getDeadLine();

            return BoardResponseDto.ReadAllBoardResponseDto.builder()
                .title(board.getTitle())
                .memberNickname(memberNickname)
                .boardCnt(boardCnt)
                .likeCnt(likeCnt)
                .deadLine(deadLine)
                .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean updateBoard(Long boardId,
        BoardRequestDto.UpdateRequestDto requestDto) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        if (!board.getMember().getMemberId().equals(requestDto.getMemberId())) {
            throw new BoardUnauthorizedException(BoardErrorCode.UNAUTHORIZED_TO_UPDATE_BOARD);
        }

        log.info("요청에 따라 게시글을 수정합니다.(Update the post as requested.)");

        // 요청된 데이터로 수정할 내용 업데이트
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setCategoryBoard(requestDto.getCategoryBoard());
        board.setDeadLine(requestDto.getDeadLine());

        // 수정된 게시글을 저장
        Board updateBoard = boardRepository.save(board);
        if (updateBoard == null) {
            throw new BoardInternalServerException(BoardErrorCode.BOARD_UPDATE_FAILED);
        }
        return true;
    }

    @Override
    public Boolean deleteBoard(Long boardId) {

        Board board = boardRepository.findByBoardId(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));

        log.info("요청에 따라 게시글을 삭제합니다.(Delete the post as requested.)");

        boardRepository.deleteById(boardId);
        if (boardRepository.existsById(boardId)) {
            throw new BoardInternalServerException(BoardErrorCode.BOARD_DELETE_FAILED);
        }
        return true;
    }
}