package com.kakao.saramaracommunity.attach.service.dto.request;

import com.kakao.saramaracommunity.attach.entity.AttachType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * AttachServiceRequest: Business Layer에서 Attach(첨부 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachServiceRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UploadRequest {

        private AttachType attachType;

        private Long ids;

        private Map<Long, String> imgList;

        @Builder
        public UploadRequest(AttachType attachType, Long ids, Map<Long, String> imgList) {
            this.attachType = attachType;
            this.ids = ids;
            this.imgList = imgList;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetBoardImageRequest {

        AttachType attachType;

        Long ids;

        @Builder
        private GetBoardImageRequest(AttachType attachType, Long ids) {
            this.attachType = attachType;
            this.ids = ids;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {

        private Long attachId;

        private AttachType attachType;

        private Long ids;

        private String imgPath;

        @Builder
        private UpdateRequest(Long attachId, AttachType attachType, Long ids, String imgPath) {
            this.attachId = attachId;
            this.attachType = attachType;
            this.ids = ids;
            this.imgPath = imgPath;
        }
    }

}
