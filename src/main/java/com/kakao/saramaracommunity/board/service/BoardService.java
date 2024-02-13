package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardUpdateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardSearchResponse;
import com.kakao.saramaracommunity.board.entity.SortType;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardCreateResponse createBoard(BoardCreateServiceRequest request);
    BoardGetResponse getBoard(Long boardId);
    BoardSearchResponse searchBoards(Long cursorId, Pageable page, SortType sort);
    void updateBoard(Long boardId, BoardUpdateServiceRequest request);
    void deleteBoard(Long boardId);
}
