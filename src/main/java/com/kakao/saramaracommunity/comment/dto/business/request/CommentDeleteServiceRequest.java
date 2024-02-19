package com.kakao.saramaracommunity.comment.dto.business.request;

import java.util.Objects;
import lombok.Builder;

@Builder
public record CommentDeleteServiceRequest(Long memberId) {

}
