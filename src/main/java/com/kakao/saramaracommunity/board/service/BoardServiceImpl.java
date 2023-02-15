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

    // Board Register
    @Override
    public Long boardRegister(BoardDTO boardDTO) {
        log.info("Service: " + boardDTO);
        Board board = dtoToEntity(boardDTO);
        boardRepository.save(board);
        return board.getId();
    }

    @Override
    public BoardPageResponseDTO<BoardDTO, Object[]> getBoardList(BoardPageRequestDTO boardPageRequestDTO) {
        log.info("board: " + boardPageRequestDTO);

        Function<Object [], BoardDTO> fn = (ent -> entityToDTO(
                (Board)ent[0], (Member)ent[1], (Long)ent[2]));

        Page<Object []> result = boardRepository.getBoardWithCommentCount(
                boardPageRequestDTO.getPageable(
                        Sort.by("boardId").descending()));
        return new BoardPageResponseDTO<>(result, fn);
    }
}