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

    @Test
    public void saveWithMemberAndBoard() {
        // given

        // 4번 게시글에 4번 사용자가 댓글을 등록하기 위해 Board 객체 데이터 가져오기
        // Board 객체를 만들 때 생성자로 만드는 것이 아니라 DB 검색으로 가져온다
        Optional<Member> member = memberRepository.findById(4L);
        Optional<Board> board = boardRepository.findById(4L);

        Comment comment = Comment.builder()
                .member(member.get())
                .board(board.get())
                .content("test!")
                .build();
        commentRepository.save(comment);

        // when
        Optional<Comment> result = commentRepository.findById(comment.getCommentId());
        System.out.println(result);

        // then
        Assertions.assertThat(result.get().getCommentId()).isEqualTo(comment.getCommentId());
    }
}
