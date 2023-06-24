package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.BoardDTO;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public Long boardRegister(BoardDTO boardDTO) {
        return null;
    }
}