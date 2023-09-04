package com.kakao.saramaracommunity.attach.controller.dto.request;

import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.service.dto.request.AttachServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * AttachRequest: Presentation Layer에서 Attach(첨부 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachRequest {


    /**
     * UploadRequest: 게시글에 대한 이미지를 등록하기 위한 요청 DTO 클래스
     * attachType: 게시글 및 댓글 유형 여부
     * ids: 게시글이나 댓글의 번호(PK)
     * imgList: 이미지의 순서(Long)와 S3 이미지 객체 URL(String)이 담긴 Map
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UploadRequest {

        @NotNull(message = "어떤 유형의 글에서 이미지가 등록되었는지 알 수 없습니다.")
        private AttachType attachType;

        @NotNull(message = "게시글이나 댓글의 번호값을 알 수 없습니다.")
        private Long ids;

        @NotNull(message = "이미지 목록이 비어있습니다.")
        private Map<Long, String> imgList;

        @Builder
        public UploadRequest(AttachType attachType, Long ids, Map<Long, String> imgList) {
            this.attachType = attachType;
            this.ids = ids;
            this.imgList = imgList;
        }

        public AttachServiceRequest.UploadRequest toServiceRequest() {
            return AttachServiceRequest.UploadRequest.builder()
                    .attachType(attachType)
                    .ids(ids)
                    .imgList(imgList)
                    .build();
        }

    }

    /**
     * GetBoardImageRequest: 하나의 게시글에 대한 이미지를 가져오기 위한 요청 DTO 클래스
     * attachType: 게시글 및 댓글 유형 여부
     * ids: 게시글이나 댓글의 번호(PK)
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class GetBoardImageRequest {

        @NotNull(message = "어떤 유형의 글에서 이미지가 등록되었는지 알 수 없습니다.")
        AttachType attachType;

        @NotNull(message = "게시글이나 댓글의 번호값을 알 수 없습니다.")
        Long ids;

        @Builder
        private GetBoardImageRequest(AttachType attachType, Long ids) {
            this.attachType = attachType;
            this.ids = ids;
        }

        public AttachServiceRequest.GetBoardImageRequest toServiceRequest() {
            return AttachServiceRequest.GetBoardImageRequest.builder()
                    .attachType(attachType)
                    .ids(ids)
                    .build();
        }

    }

    /**
     * UpdateRequest: 하나의 게시글에 대한 이미지를 수정하기 위한 요청 DTO 클래스
     * attachType: 게시글 및 댓글 유형 여부
     * ids: 게시글이나 댓글의 번호(PK)
     * imgList: 이미지의 순서(Long)와 S3 이미지 객체 URL(String)이 담긴 Map
     *
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UpdateRequest {

        @NotNull(message = "이미지 첨부파일 번호를 알 수 없습니다.")
        private Long attachId;

        @NotNull(message = "어떤 유형의 글에서 이미지가 등록되었는지 알 수 없습니다.")
        private AttachType attachType;

        @NotNull(message = "게시글이나 댓글의 번호값을 알 수 없습니다.")
        private Long ids;

        private String imgPath;

        @Builder
        private UpdateRequest(Long attachId, AttachType attachType, Long ids, String imgPath) {
            this.attachId = attachId;
            this.attachType = attachType;
            this.ids = ids;
            this.imgPath = imgPath;
        }

        public AttachServiceRequest.UpdateRequest toServiceRequest() {
            return AttachServiceRequest.UpdateRequest.builder()
                    .attachId(attachId)
                    .attachType(attachType)
                    .ids(ids)
                    .imgPath(imgPath)
                    .build();
        }

    }

}
