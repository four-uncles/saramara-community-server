package com.kakao.saramaracommunity.bucket.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * BucketResponse: Bucket과 관련된 응답 데이터를 담을 DTO 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class BucketResponse {

    @Getter
    public static class BucketUploadResponse {

        private Integer code;
        private String msg;
        private List<String> data;

        @Builder
        private BucketUploadResponse(Integer code, String msg, List<String> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        public static BucketUploadResponse of(int httpStatusCode, String message, List<String> data) {
            return BucketUploadResponse.builder()
                    .code(httpStatusCode)
                    .msg(message)
                    .data(data)
                    .build();
        }

    }

}
