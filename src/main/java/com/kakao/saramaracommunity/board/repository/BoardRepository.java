package com.kakao.saramaracommunity.board.repository;

import java.util.Optional;

import com.kakao.saramaracommunity.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardIdAndDeletedAtIsNull(Long boardId);
}