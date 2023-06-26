package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardService {

    //게시글 등록 요청 Method
    Board saveBoard(BoardRequestDto.SaveRequestDto requestDto);
}