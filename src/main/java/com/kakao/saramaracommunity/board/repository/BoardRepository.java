package com.kakao.saramaracommunity.board.repository;

import com.kakao.saramaracommunity.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /* Board Data를 가져올 때 Member의 데이터도 Join */
    @Query("select b, m from Board b left join Member m where b.boardId=:id")
    public Object getBoardWithMember(@Param("id") Long id);

}