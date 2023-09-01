package com.kakao.saramaracommunity.board.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardId(Long boardId);

    List<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Board> findByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable page);

    // List<Board> findAllByOrderByLikeCntDesc(Pageable pageable);
    //
    // List<Board> findByBoardIdLessThanOrderByLikeCntDesc(Long boardId, Pageable page);

    Boolean existsByBoardIdLessThan(Long boardId);
}