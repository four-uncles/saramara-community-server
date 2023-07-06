package com.kakao.saramaracommunity.board.service;

import java.time.LocalDateTime;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.entity.Board;
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
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

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

        Board board = boardRepository.findByBoardIdAndDeletedAtIsNull(boardId)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

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
}