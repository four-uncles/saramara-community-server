package com.kakao.saramaracommunity.vote.service;

import com.kakao.saramaracommunity.vote.dto.business.request.VoteCreateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;

public interface VoteService {

    VoteCreateResponse createVote(VoteCreateServiceRequest request);

}
