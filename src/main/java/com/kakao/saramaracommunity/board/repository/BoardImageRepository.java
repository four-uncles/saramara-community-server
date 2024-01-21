package com.kakao.saramaracommunity.board.repository;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoardId(Long boardId);
}