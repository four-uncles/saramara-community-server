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

    /**
     * UploadRequestDeprecated: 게시글에 대한 이미지를 등록하기 위한 요청 DTO 클래스
     * attachType: 게시글 및 댓글 유형 여부
     * ids: 게시글이나 댓글의 번호(PK)
     * imgList: 이미지의 순서(Long)와 이미지 파일(MultipartFile)이 담긴 Map
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UploadRequestDeprecated {

        private AttachType attachType;

        private Long ids;

        private Map<Long, MultipartFile> imgList;

        @Builder
        public UploadRequestDeprecated(AttachType attachType, Long ids, Map<Long, MultipartFile> imgList) {
            this.attachType = attachType;
            this.ids = ids;
            this.imgList = imgList;
        }

    }

    /**
     * UploadRequest: 게시글에 대한 이미지를 등록하기 위한 요청 DTO 클래스
     * attachType: 게시글 및 댓글 유형 여부
     * ids: 게시글이나 댓글의 번호(PK)
     * imgList: 이미지의 순서(Long)와 S3 이미지 객체 URL(String)이 담긴 Map
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
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

    /**
     * UploadBucketRequest: 게시글의 이미지 목록을 AWS S3에 등록하기 위한 요청 DTO 클래스
     * imgList: 이미지 파일(MultipartFile)이 담긴 ArrayList
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UploadBucketRequest {

        private List<MultipartFile> imgList;

        @Builder
        private UploadBucketRequest(List<MultipartFile> imgList) {
            this.imgList = imgList;
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

        AttachType attachType;

        Long ids;

        @Builder
        private GetBoardImageRequest(AttachType attachType, Long ids) {
            this.attachType = attachType;
            this.ids = ids;
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
