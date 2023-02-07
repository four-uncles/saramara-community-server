package com.kakao.saramaracommunity.board.repository;

import com.kakao.saramaracommunity.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}