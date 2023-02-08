package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public Long register(CommentDTO commentDTO) {

        Comment comment = dtoToEntity(commentDTO);

        Long cno = commentRepository.save(comment).getCommentId();

        return cno;
    }
}
