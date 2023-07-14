package com.kakao.saramaracommunity.attach.service;


import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;

/**
 * AttachService: 이미지 첨부파일 관련 비즈니스 로직을 수행할 서비스 인터페이스
 *
 * @author Taejun
 * @version 0.0.1
 */
public interface AttachService {

    AttachResponse.UploadResponse uploadImage(AttachRequest.UploadRequest request);

    AttachResponse.GetImageResponse getBoardImages(AttachRequest.GetBoardImageRequest request);

    AttachResponse.GetAllImageResponse getAllBoardImages();
}
