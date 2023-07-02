package com.kakao.saramaracommunity.attach.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * AttachResponse: uploadImage 메서드의 응답 데이터를 담을 DTO 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachResponse {

    private String code;
    private String msg;
    private boolean data;

    @Builder
    public AttachResponse(String code, String msg, boolean data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
