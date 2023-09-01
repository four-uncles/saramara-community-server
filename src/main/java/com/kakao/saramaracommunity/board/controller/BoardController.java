package com.kakao.saramaracommunity.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.service.BoardService;
import com.kakao.saramaracommunity.board.service.dto.response.BoardResponseDto;
import com.kakao.saramaracommunity.board.util.CursorResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;
    private static final int DEFAULT_PAGE_SIZE = 3;

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

    @GetMapping
    public ResponseEntity<CursorResult<BoardResponseDto.ReadAllBoardResponseDto>> readAllBoardsByLatest(
        @RequestParam(name = "cursorId", required = false) Long cursorId,
        @RequestParam(name = "size", required = false) Integer size
    ) {
        if (size == null) size = DEFAULT_PAGE_SIZE;
        Pageable page = PageRequest.of(0, size);

        CursorResult<BoardResponseDto.ReadAllBoardResponseDto> cursorResult =
            boardService.readAllBoardsByLatest(cursorId, page);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Has-Next", cursorResult.getHasNext().toString());
        Long nextCursorId = cursorResult.getNextCursorId();
        if (nextCursorId != null) {
            headers.add("X-Next-Cursor-Id", nextCursorId.toString());
        }

        return ResponseEntity.ok().headers(headers).body(cursorResult);
    }

    @GetMapping("/popular")
    public ResponseEntity<CursorResult<BoardResponseDto.ReadAllBoardResponseDto>> readAllBoardsByPopularity(
        @RequestParam(name = "cursorId", required = false) Long cursorId,
        @RequestParam(name = "size", required = false) Integer size
    ) {
        if (size == null) size = DEFAULT_PAGE_SIZE;
        Pageable page = PageRequest.of(0, size);

        CursorResult<BoardResponseDto.ReadAllBoardResponseDto> cursorResult =
            boardService.readAllBoardsByPopularity(cursorId, page);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Has-Next", cursorResult.getHasNext().toString());
        Long nextCursorId = cursorResult.getNextCursorId();
        if (nextCursorId != null) {
            headers.add("X-Next-Cursor-Id", nextCursorId.toString());
        }

        return ResponseEntity.ok().headers(headers).body(cursorResult);
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
