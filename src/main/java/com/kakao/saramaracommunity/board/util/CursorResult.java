package com.kakao.saramaracommunity.board.util;

import java.util.List;

import lombok.Getter;

@Getter
public class CursorResult<T> {
    private final List<T> values;

    private final Boolean hasNext;

    private final Long nextCursorId;

    public CursorResult(List<T> values, Boolean hasNext, Long nextCursorId) {
        this.values = values;
        this.hasNext = hasNext;
        this.nextCursorId = nextCursorId;
    }
}
