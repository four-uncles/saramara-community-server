package com.kakao.saramaracommunity.board.service;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.Category;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Board saveBoard(BoardRequestDto.SaveRequestDto requestDto) {

        // 요청된 데이터로 Member 객체를 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 카테고리 값 설정
        Category category = requestDto.getCategory();

        // Board Entity 생성
        Board board = Board.builder()
            .member(member)
            .category(category)
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .build();

        return boardRepository.save(board);
    }
}