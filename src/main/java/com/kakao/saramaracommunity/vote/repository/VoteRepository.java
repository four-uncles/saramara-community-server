package com.kakao.saramaracommunity.vote.repository;

import com.kakao.saramaracommunity.vote.entity.Vote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    /**
     * 게시글의 이미지에 대한 투표 수 조회
     *
     * @param boardId 게시글 고유 식별자
     * @return 이미지 경로 - 투표 수의 객체 목록
     */
    @Query("SELECT i.path, COALESCE(COUNT(v), 0) " +
            "FROM BoardImage i " +
            "LEFT JOIN Vote v ON i.id = v.boardImage.id AND v.board.id = :boardId " +
            "GROUP BY i.path")
    List<Object[]> getVotesByBoard(@Param("boardId") Long boardId);

    Optional<Vote> findByMemberIdAndBoardId(Long memberId, Long boardId);

}
