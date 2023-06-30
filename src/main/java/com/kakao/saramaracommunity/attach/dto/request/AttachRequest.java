package com.kakao.saramaracommunity.attach.dto.request;

import com.kakao.saramaracommunity.attach.entity.AttachType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * AttachRequest: Attach(첨부 이미지) 관련 요청 DTO를 관리하는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
public class AttachRequest {

    /**
     * UploadRequest: uploadImage 메서드의 요청 데이터를 담을 DTO 클래스
     * type: 게시글 및 댓글 유형 여부
     * id: 게시글이나 댓글의 번호(PK)
     * imgList: 이미지의 순서(Long)와 이미지 파일(MultipartFile)이 담긴 Map
     */
    @Getter
    public static class UploadRequest {

        @NotNull(message = "어떤 유형의 글에서 이미지가 등록되었는지 알 수 없습니다.")
        private AttachType type;

        @NotNull(message = "게시글이나 댓글의 번호값을 알 수 없습니다.")
        private Long id;

        private Map<Long, MultipartFile> imgList;

        @Builder
        public UploadRequest(AttachType type, Long id, Map<Long, MultipartFile> imgList) {
            this.type = type;
            this.id = id;
            this.imgList = imgList;
        }

    }

}
