package com.kakao.saramaracommunity.vote.service;

import com.kakao.saramaracommunity.vote.dto.business.request.VoteCreateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteUpdateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.dto.business.response.VotesReadInBoardResponse;

public interface VoteService {

    VoteCreateResponse createVote(VoteCreateServiceRequest request);

    VotesReadInBoardResponse readVoteInBoard(Long memberId, Long boardId);

    void updateVote(Long voteId, VoteUpdateServiceRequest request);

}
