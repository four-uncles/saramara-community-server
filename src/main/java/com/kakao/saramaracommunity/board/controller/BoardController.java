package com.kakao.saramaracommunity.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.board.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/register")
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequestDto.SaveRequestDto requestDto) {
        Board savedBoard = boardService.saveBoard(requestDto);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");

        // 게시글 정보
        response.put("data", savedBoard);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Object> readOneBoard(@PathVariable Long boardId) {
        BoardResponseDto.ReadOneBoardResponseDto boardResponseDto = boardService.readOneBoard(boardId);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");

        // 게시글 정보
        response.put("data", boardResponseDto);

        return ResponseEntity.ok(response);
    }

}
