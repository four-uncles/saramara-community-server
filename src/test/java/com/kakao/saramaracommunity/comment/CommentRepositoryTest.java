package com.kakao.saramaracommunity.comment;

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
    public void 댓글_등록() {
        // given

        commentRepository.save(Comment.builder()
                        .text("Test Text")
                        .pick(0)
                        .build());

        // when
        List<Comment> commentList = commentRepository.findAll();

        // then
        Comment comment = commentList.get(0);
        System.out.println(comment);
        Assertions.assertThat(comment.getCommentId()).isEqualTo(1L);
    }
}
