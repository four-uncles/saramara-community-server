package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardService {

    //게시글 등록 요청 Method
    Board saveBoard(BoardRequestDto.SaveRequestDto requestDto);

    // boardId를 매개변수로 받아 해당 게시글을 조회하는 기능의 Method
    BoardResponseDto.ReadOneBoardResponseDto readOneBoard(Long boardId);
}