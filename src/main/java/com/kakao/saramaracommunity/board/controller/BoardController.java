package com.kakao.saramaracommunity.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import com.kakao.saramaracommunity.common.dto.Payload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;
    private static final int DEFAULT_PAGE_SIZE = 24;

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

    /**
     * 모든 게시물을 조회하는 엔드포인트 입니다.
     * @param cursorId 다음 페이지의 커서 ID
     * @param size 페이지 크기
     * @param sort 정렬 방식 - 최신순(latest), 인기순(popular)
     * @return ResponseEntity<Object> 객체
     */
    @GetMapping
    public ResponseEntity<Object> readAllBoards(
        @RequestParam(name = "cursorId", required = false) Long cursorId,
        @RequestParam(name = "size", required = false) Integer size,
        @RequestParam(name = "sort", defaultValue = "latest") String sort
    ) {
        if (size == null) size = DEFAULT_PAGE_SIZE;
        Pageable page = PageRequest.of(0, size);

        BoardResponseDto.ReadPageBoardResponseDto readPage;
        if ("popular".equals(sort)) readPage = boardService.readAllBoardsByPopularity(cursorId, page);
        else readPage = boardService.readAllBoardsByLatest(cursorId, page);

        Payload<BoardResponseDto.ReadPageBoardResponseDto> resPayload = Payload.successPayload(
            HttpStatus.OK.value(),
            "success",
            readPage
        );

        return ResponseEntity.ok().body(resPayload);
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
