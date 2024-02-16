package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardCreateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardDeleteServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.reqeust.BoardUpdateServiceRequest;
import com.kakao.saramaracommunity.board.dto.business.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.dto.business.response.BoardSearchResponse;
import com.kakao.saramaracommunity.board.entity.SortType;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardSearchResponse searchBoards(Long cursorId, Pageable page, SortType sort);
    BoardGetResponse getBoard(Long boardId);
    BoardCreateResponse createBoard(BoardCreateServiceRequest request);
    void updateBoard(Long boardId, BoardUpdateServiceRequest request);
    void deleteBoard(Long boardId, BoardDeleteServiceRequest request);
}
