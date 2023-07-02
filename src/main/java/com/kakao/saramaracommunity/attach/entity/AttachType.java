package com.kakao.saramaracommunity.attach.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttachType {

    BOARD("게시글"),
    COMMENT("댓글");

    private final String text;

}
