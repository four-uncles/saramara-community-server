package com.kakao.saramaracommunity.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * SELECT * FROM Board
     * WHERE boardId = :boardId
     */
    Optional<Board> findByBoardId(Long boardId);

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
    List<Board> findByBoardIdLessThanOrderByCreatedAtDesc(Long boardId, Pageable page);

    /**
     * SELECT * FROM Board
     * ORDER BY like_cnt DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findAllByOrderByLikeCntDesc(Pageable page);

    /**
     * SELECT * FROM Board
     * WHERE like_cnt < :likeCnt
     * ORDER BY like_cnt DESC
     * LIMIT :pageSize OFFSET :pageNumber
     */
    List<Board> findByLikeCntLessThanOrderByLikeCntDesc(Long LikeCnt, Pageable page);
}