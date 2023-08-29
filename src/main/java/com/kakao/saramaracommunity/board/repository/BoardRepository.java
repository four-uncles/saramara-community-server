package com.kakao.saramaracommunity.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardId(Long boardId);

    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    Page<Board> findAllOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT b FROM Board b ORDER BY b.likeCnt DESC")
    Page<Board> findAllOrderByLikeCntDesc(Pageable pageable);
}