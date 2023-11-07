package com.kakao.saramaracommunity.board.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * SELECT * FROM Board
     * ORDER BY created_at DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findAllByOrderByCreatedAtDesc(Pageable page);

    /**
     * SELECT * FROM Board
     * WHERE boardId < :boardId
     * ORDER BY created_at DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findByIdLessThanOrderByCreatedAtDesc(Long boardId, Pageable page);

    /**
     * SELECT * FROM Board
     * ORDER BY like_cnt DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findAllByOrderByLikeCountDesc(Pageable page);

    /**
     * SELECT * FROM Board
     * WHERE like_cnt < :likeCnt
     * ORDER BY like_cnt DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findByLikeCountLessThanOrderByLikeCountDesc(Long LikeCnt, Pageable page);
}