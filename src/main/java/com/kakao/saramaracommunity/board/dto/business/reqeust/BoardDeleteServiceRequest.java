package com.kakao.saramaracommunity.board.dto.business.reqeust;

import lombok.Builder;

@Builder
public record BoardDeleteServiceRequest(
        Long memberId
) {

}
