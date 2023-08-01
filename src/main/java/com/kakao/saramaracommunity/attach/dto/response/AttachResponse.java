package com.kakao.saramaracommunity.attach.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * AttachResponse: uploadImage 메서드의 응답 데이터를 담을 DTO 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachResponse {


    @Getter
    public static class UploadResponse {

        private String code;

        private String msg;

        private boolean data;

        @Builder
        private UploadResponse(String code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class UploadBucketResponse {

        private String code;

        private String msg;

        private List<String> data;

        @Builder
        private UploadBucketResponse(String code, String msg, List<String> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class GetImageResponse {

        private String code;

        private String msg;

        private Map<Long, Map<Long, String>> data;

        @Builder
        private GetImageResponse(String code, String msg, Map<Long, Map<Long, String>> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class GetAllImageResponse {

        private String code;

        private String msg;

        private Map<Long, Map<Long, String>> data;

        @Builder
        private GetAllImageResponse(String code, String msg, Map<Long, Map<Long, String>> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

    }

    @Getter
    public static class UpdateResponse {

        private String code;

        private String msg;

        private boolean data;

        @Builder
        private UpdateResponse(String code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
    }

    @Getter
    public static class DeleteResponse {

        private String code;

        private String msg;

        private boolean data;

        @Builder
        private DeleteResponse(String code, String msg, boolean data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
    }
}
