package com.kakao.saramaracommunity.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * board 테이블에서 boardId로 일치하는 게시글을 찾습니다.
     * findByBoardId
     * SELECT * FROM Board WHERE boardId = :boardId
     * @param boardId 게시글 ID
     * @return Optional<Board> 객체
     */
    Optional<Board> findByBoardId(Long boardId);

    /**
     * board 테이블에서 createdAt을 기준으로 내림차순하여 모든 게시글을 조회합니다.
     * findAllByOrderByCreatedAtDesc
     * SELECT * FROM Board ORDER BY created_at DESC LIMIT :pageSize OFFSET :pageNumber
     * @param page 페이지 정보
     * @return 게시글 목록
     */
    List<Board> findAllByOrderByCreatedAtDesc(Pageable page);

    /**
     * board 테이블에서 boardId가 주어진 boardId보다 작은 값을 선택하고, 선택된 결과를 createdAt의 기준으로 내림차순 정렬을 합니다.
     * findByBoardIdLessThanOrderByCreatedAtDesc
     * SELECT * FROM Board WHERE boardId < :boardId ORDER BY created_at DESC LIMIT :pageSize OFFSET :pageNumber
     * @param boardId 게시글 ID
     * @param page 페이지 정보
     * @return 게시글 목록
     */
    List<Board> findByBoardIdLessThanOrderByCreatedAtDesc(Long boardId, Pageable page);

    /**
     * board 테이블에서 likeCnt를 기준으로 내림차순으로 정렬합니다.
     * findAllByOrderByLikeCntDesc
     * SELECT * FROM Board ORDER BY like_cnt DESC LIMIT :pageSize OFFSET :pageNumber
     * @param page 페이지 정보
     * @return 게시글 목록
     */
    List<Board> findAllByOrderByLikeCntDesc(Pageable page);

    /**
     * board 테이블에서 likeCnt가 주어진 likeCnt보다 작은 값을 선택하고, 선택된 결과를 likeCnt 기준으로 내림차순으로 정렬합니다.
     * findByLikeCntLessThanOrderByLikeCntDesc
     * SELECT * FROM Board WHERE like_cnt < :likeCnt ORDER BY like_cnt DESC LIMIT :pageSize OFFSET :pageNumber
     * @param LikeCnt 좋아요 수
     * @param page 페이지 정보
     * @return 게시글 목록
     */
    List<Board> findByLikeCntLessThanOrderByLikeCntDesc(Long LikeCnt, Pageable page);
}