package com.kakao.saramaracommunity.comment.service;

import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.comment.dto.CommentDTO;
import com.kakao.saramaracommunity.comment.dto.CommentListDTO;
import com.kakao.saramaracommunity.comment.entity.Comment;
import com.kakao.saramaracommunity.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final ModelMapper modelMapper;

    /**
     * 댓글 생성을 위한 메서드입니다.
     *
     * @param commentDTO 생성에 필요한 commentDTO 입니다.
     * @return 생성된 comment의 고유 id를 반환해줍니다.
     */
    @Override
    public Long createComment(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        Long cid = commentRepository.save(comment).getCommentId();

        return cid;
    }

    /**
     * 특정 보드에 대한 모든 댓글을 가져오는 메서드입니다.
     * Stream객체를 이용하여 Comment타입 리스트를 불러온 데이터들을 반환타입인 CommentListDTO 형식으로 convert 해줍니다.
     *
     * @param boardId 특정 보드에 대한 고유id 파라미터입니다.
     * @return 해당 보드에 대한 모든 댓글들을 반환해줍니다.
     */
    @Override
    public List<CommentListDTO> getBoardComments(Long boardId) {

        List<Comment> comments = commentRepository.getCommentsByBoard(boardId);

        List<CommentListDTO> result = comments.stream()
                .map(comment -> modelMapper.map(comment, CommentListDTO.class))
                .collect(Collectors.toList());


        return result;
    }
}
