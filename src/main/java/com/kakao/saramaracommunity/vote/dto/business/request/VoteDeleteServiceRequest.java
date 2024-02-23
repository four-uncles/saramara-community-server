package com.kakao.saramaracommunity.vote.dto.business.request;

import lombok.Builder;

@Builder
public record VoteDeleteServiceRequest(Long memberId) {

}
