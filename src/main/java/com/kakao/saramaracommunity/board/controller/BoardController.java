package com.kakao.saramaracommunity.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.board.controller.dto.request.BoardRequestDto;
import com.kakao.saramaracommunity.board.service.dto.response.BoardResponseDto;
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
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardRequestDto.SaveRequestDto request) {
        Board savedBoard = boardService.saveBoard(request.toServiceRequest());

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

    @GetMapping("/")
    public ResponseEntity<Object> readAllBoardsByLatest(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "24") int size) {
        Page<BoardResponseDto.ReadAllBoardResponseDto> boardPage = boardService.readAllBoardsByLatest(
            PageRequest.of(page, size)
        );

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");

        // 게시글 목록
        response.put("data", boardPage);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<Object> readAllBoardsByPopularity(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "24") int size) {
        Page<BoardResponseDto.ReadAllBoardResponseDto> boardPage = boardService.readAllBoardsByPopularity(
            PageRequest.of(page, size)
        );

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");

        // 게시글 목록
        response.put("data", boardPage);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Object> updateBoard(
            @PathVariable ("boardId") Long boardId,
            @RequestBody @Valid BoardRequestDto.UpdateRequestDto request
    ) {

        Boolean result = boardService.updateBoard(boardId, request.toServiceRequest());

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "success");
        response.put("result", result);

        return ResponseEntity.status((int)response.get("code")).body(response);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable ("boardId") Long boardId) {

        Boolean deletedBoard = boardService.deleteBoard(boardId);

        Map<String, Object> result = new HashMap<>();

        result.put("result", deletedBoard);

        return ResponseEntity.ok(result);
    }
}
