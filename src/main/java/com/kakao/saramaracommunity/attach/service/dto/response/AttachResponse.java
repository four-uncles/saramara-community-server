package com.kakao.saramaracommunity.attach.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * AttachResponse: Attach와 관련된 응답 데이터를 담을 DTO 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachResponse {


    @Getter
    public static class UploadResponse {

        private Integer code;

        private String msg;

        private boolean data;

        @Builder
        private UploadResponse(Integer code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class UploadBucketResponse {

        private Integer code;

        private String msg;

        private List<String> data;

        @Builder
        private UploadBucketResponse(Integer code, String msg, List<String> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class GetImageResponse {

        private Integer code;

        private String msg;

        private Map<Long, Map<Long, String>> data;

        @Builder
        private GetImageResponse(Integer code, String msg, Map<Long, Map<Long, String>> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class GetAllImageResponse {

        private Integer code;

        private String msg;

        private Map<Long, Map<Long, String>> data;

        @Builder
        private GetAllImageResponse(Integer code, String msg, Map<Long, Map<Long, String>> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class UpdateResponse {

        private Integer code;

        private String msg;

        private boolean data;

        @Builder
        private UpdateResponse(Integer code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
    }

    @Getter
    public static class DeleteResponse {

        private Integer code;

        private String msg;

        private boolean data;

        @Builder
        private DeleteResponse(Integer code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
    }
}
