package com.kakao.saramaracommunity.board.service;

import org.springframework.data.domain.Pageable;

import com.kakao.saramaracommunity.board.entity.SortType;
import com.kakao.saramaracommunity.board.service.dto.request.BoardServiceRequest;
import com.kakao.saramaracommunity.board.service.dto.response.BoardResponse;

public interface BoardService {

    BoardResponse.BoardCreateResponse createBoard(BoardServiceRequest.BoardCreateServiceRequest requestDto);

    BoardResponse.BoardGetResponse getBoard(Long boardId);

    BoardResponse.BoardSearchResponse searchBoards(Long cursorId, Pageable page, SortType sort);

    void updateBoard(Long boardId, BoardServiceRequest.BoardUpdateServiceRequest requestDto);

    void deleteBoard(Long boardId);
}