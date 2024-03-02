package com.kakao.saramaracommunity.vote.controller;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteCreateRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteDeleteRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteUpdateRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.dto.business.response.VotesReadInBoardResponse;
import com.kakao.saramaracommunity.vote.service.VoteService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {

    private final VoteService voteService;

    /**
     * 투표 생성 API
     * @param request memberId: 투표자 고유 식별자, boardId: 게시글 고유 식별자, boardImage: 투표할 이미지 고유 식별자
     * @return "code", "message", "data": { "voter", "boardId", "boardImageId"}
     */
    @PostMapping
    public ResponseEntity<ApiResponse<VoteCreateResponse>> createVote(
            @Valid @RequestBody VoteCreateRequest request
    ) {
        VoteCreateResponse data = voteService.createVote(request.toServiceRequest());

        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 투표를 완료하였습니다.",
                        data
                )
        );

    }

    /**
     * 투표 조회 API
     * @param boardId 투표 상태가 저장된 게시글 고유 식별자
     * @return "code", "message", "data" : { "boardId", "isVoted", "totalVotes", "voteCounts"}
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<VotesReadInBoardResponse>> getBoardVotes(
            @PathVariable("boardId") Long boardId,
            Principal principal
    ) {
        VotesReadInBoardResponse data = voteService.readVoteInBoard(boardId, principal);
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 게시글의 투표 상태를 조회하였습니다.",
                        data
                )
        );
    }

    /**
     * 투표 수정 API
     * @param voteId 투표 고유 식별자
     * @param request memberId: 작성자 고유 식별자, boardImage: 재선택 이미지
     * @return "code", "message"
     */
    @PatchMapping("/{voteId}")
    public ResponseEntity<ApiResponse> updateVote(
            @PathVariable("voteId") Long voteId,
            @Valid @RequestBody VoteUpdateRequest request
    ) {
        voteService.updateVote(voteId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 투표를 수정 하였습니다."
                )
        );
    }

    /**
     * 투표 삭제 API
     * @param voteId 투표 고유 식별자
     * @param request memberId: 투표자의 고유 식별자
     * @return "code", "message"
     */
    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiResponse> deleteVote(
            @PathVariable("voteId") Long voteId,
            @Valid @RequestBody VoteDeleteRequest request
    ) {
        voteService.deleteVote(voteId, request.toServiceRequest());
        return ResponseEntity.ok().body(
                ApiResponse.successResponse(
                        HttpStatus.OK,
                        "성공적으로 투표를 삭제 하였습니다."
                )
        );
    }

}
