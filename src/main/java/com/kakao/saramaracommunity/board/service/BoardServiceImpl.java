package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.BoardDTO;
import com.kakao.saramaracommunity.board.dto.BoardPageRequestDTO;
import com.kakao.saramaracommunity.board.dto.BoardPageResponseDTO;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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