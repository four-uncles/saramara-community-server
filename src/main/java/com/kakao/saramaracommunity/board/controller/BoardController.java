package com.kakao.saramaracommunity.board.controller;

import com.kakao.saramaracommunity.board.controller.dto.reqeust.BoardCreateRequest;
import com.kakao.saramaracommunity.board.controller.dto.reqeust.BoardDeleteRequest;
import com.kakao.saramaracommunity.board.controller.dto.reqeust.BoardUpdateRequest;
import com.kakao.saramaracommunity.board.controller.dto.response.BoardCreateResponse;
import com.kakao.saramaracommunity.board.controller.dto.response.BoardGetResponse;
import com.kakao.saramaracommunity.board.controller.dto.response.BoardSearchResponse;
import com.kakao.saramaracommunity.board.entity.SortType;
import com.kakao.saramaracommunity.board.service.BoardService;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;
    private static final int DEFAULT_PAGE_SIZE = 24;

    /**
     * 모든 게시물을 조회하는 API 엔드포인트 입니다.
     * @param cursorId 다음 페이지의 커서 ID
     * @param size     페이지 크기
     * @param sort     정렬 방식 - 최신순(latest), 인기순(popular)
     */
    @Operation(summary = "모든 게시글 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<BoardSearchResponse>> searchBoards(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", defaultValue = "LATEST") SortType sort
    ) {
        if (size == null) size = DEFAULT_PAGE_SIZE;
        BoardSearchResponse response = boardService.searchBoards(cursorId, PageRequest.of(0, size), sort);
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        OK,
                        "성공적으로 모든 게시글 정보를 조회하였습니다.",
                        response
                )
        );
    }

    @Operation(summary = "단일 게시글 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardGetResponse>> getBoard(
            @PathVariable("boardId") Long boardId
    ) {
        BoardGetResponse response = boardService.getBoard(boardId);
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        OK,
                        "성공적으로 게시글 정보를 조회하였습니다.",
                        response
                )
        );
    }

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ResponseEntity<ApiResponse<BoardCreateResponse>> createBoard(
            @RequestBody @Valid BoardCreateRequest request
    ) {
        BoardCreateResponse response = boardService.createBoard(request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        OK,
                        "성공적으로 게시글 작성을 완료하였습니다.",
                        response
                )
        );
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{boardId}")
    public ResponseEntity<ApiResponse> updateBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Valid BoardUpdateRequest request
    ) {
        boardService.updateBoard(boardId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        OK,
                        "성공적으로 게시글을 수정하였습니다.",
                        true
                )
        );
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody@Valid BoardDeleteRequest request
    ) {
        boardService.deleteBoard(boardId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        NO_CONTENT,
                        "성공적으로 게시글을 삭제하였습니다.",
                        true
                )
        );
    }
}
