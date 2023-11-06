package com.kakao.saramaracommunity.comment.repository;

import com.kakao.saramaracommunity.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * @param boardId 특정 boardId에 대한 파라미터입니다.
     * @return
     * JPQL을 이용하여 boardId 파라미터에 맞는 Comment data들을 추출해옵니다.
     */
    @Query("select c from Comment c where c.board.id = :boardId")
    List<Comment> getCommentsByBoard(Long boardId);

}
