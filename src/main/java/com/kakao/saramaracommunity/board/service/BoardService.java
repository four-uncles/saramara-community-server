package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.BoardDTO;

public interface BoardService {
    //게시글 등록 요청 Method
    Long boardRegister(BoardDTO boardDTO);

    // //게시글 목록 보기
    // BoardPageResponseDTO<BoardDTO, Object[]> getBoardList(BoardPageRequestDTO boardPageRequestDTO);
    //
    // /* DTO -> Entity */
    // default Board dtoToEntity (BoardDTO boardDTO) {
    //     Member member = Member.builder()
    //             .email(boardDTO.getWriterEmail())
    //             .nickname(boardDTO.getWriterNickName())
    //             .build();
    //     Board board = Board.builder()
    //             .member(member)
    //             .id(boardDTO.getBoardId())
    //             .title(boardDTO.getTitle())
    //             .content(boardDTO.getContent())
    //             .category(boardDTO.getCategory())
    //             .build();
    //     return board;
    // }
    // /* Entity -> DTO */
    // default BoardDTO entityToDTO(Board board, Member member, Long CommentCount) {
    //     BoardDTO boardDTO = BoardDTO.builder()
    //             .boardId(board.getId())
    //             .title(board.getTitle())
    //             .content(board.getContent())
    //             .regDate(board.getCreatedAt())
    //             .modDate(board.getUpdatedAt())
    //             .writerEmail(member.getEmail())
    //             .writerNickName(member.getNickname())
    //             .commentCount(CommentCount.longValue())
    //             .build();
    //     return boardDTO;
    // }
}