package com.kakao.saramaracommunity.board.repository;

import java.util.List;
import java.util.Optional;

import com.kakao.saramaracommunity.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardIdAndDeletedAtIsNull(Long boardId);

    @Query("SELECT b FROM Board b WHERE b.deletedAt IS NULL ORDER BY b.createdAt DESC")
    List<Board> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("SELECT b FROM Board b WHERE b.deletedAt IS NULL ORDER BY b.likeCnt DESC")
    List<Board> findAllByDeletedAtIsNullOrderByLikeCntDesc();
}