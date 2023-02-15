package com.kakao.saramaracommunity.board.repository;

import com.kakao.saramaracommunity.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /* Board Data를 가져올 때 Member의 데이터도 Join */
    @Query("select b, m from Board b left join Member m where b.id=:id")
    public Object getBoardWithMember(@Param("id") Long id);


    /* BoardId를 받아서 Board와 관련된 Comment 정보 Join */
    @Query("select b, c from Board b left join Comment c on c.board = b where b.id=:id")
    List<Object []> getBoardWithComment(@Param("id") Long id);
    @Query("select b,m, count(c) from  Board b " +
            "left join b.member m " +
            "left join Comment c on c.board = b " +
            "group by b")
    Page<Object[]> getBoardWithCommentCount(Pageable pageable);
    
}