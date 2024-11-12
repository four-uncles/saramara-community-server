package com.kakao.saramaracommunity.board.service.reqeust;

import lombok.Builder;

@Builder
public record BoardDeleteServiceRequest(
        Long memberId
) {

}
