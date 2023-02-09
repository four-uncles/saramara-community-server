package com.kakao.saramaracommunity.comment.repository;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertOne() {
        // given
        commentRepository.save(Comment.builder()
                        .content("Test Text")
                        .pick(0L)
                        .build());

        // when
        List<Comment> commentList = commentRepository.findAll();

        // then
        Comment comment = commentList.get(1);
        System.out.println(comment);
        Assertions.assertThat(comment.getCommentId()).isEqualTo(2L);
    }

    @Test
    public void updateOne() {
        // given
        Long cno = 1L;

        Optional<Comment> result = commentRepository.findById(cno);

        Comment comment = result.orElseThrow();

        // when

        comment.change("Update content", 2L);
        commentRepository.save(comment);
        // then

        System.out.println(commentRepository.findById(1L));
    }

    @Test
    public void deleteOne() {
        Long cno = 1L;
        commentRepository.deleteById(cno);
    }

    @Test
    public void findOne() {
        Long cno = 2L;

        Optional<Comment> findingComment = commentRepository.findById(cno);
        Comment result = findingComment.orElseThrow();

        System.out.println("result = " + result);
    }
}
