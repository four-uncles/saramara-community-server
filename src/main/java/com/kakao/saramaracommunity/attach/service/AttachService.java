package com.kakao.saramaracommunity.attach.service;


import com.kakao.saramaracommunity.attach.service.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.service.dto.request.AttachServiceRequest;

/**
 * AttachService: 이미지 첨부파일 관련 비즈니스 로직을 수행할 서비스 인터페이스
 *
 * @author Taejun
 * @version 0.0.1
 */
public interface AttachService {

    AttachResponse.GetImageResponse getBoardImages(AttachServiceRequest.GetBoardImageRequest request);

    AttachResponse.GetAllImageResponse getAllBoardImages();

    AttachResponse.UploadResponse uploadImages(AttachServiceRequest.UploadRequest request);

    AttachResponse.UpdateResponse updateImage(AttachServiceRequest.UpdateRequest request);

    AttachResponse.DeleteResponse deleteImage(Long attachId);

}
