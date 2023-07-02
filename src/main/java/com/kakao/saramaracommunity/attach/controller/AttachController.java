package com.kakao.saramaracommunity.attach.controller;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.service.AttachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AttachController: 이미지 업로드 관련 요청을 받을 컨트롤러 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attach")
public class AttachController {

    private final AttachService attachService;

    /**
     * uploadImage: 이미지 업로드 API
     * URL: /api/v1/attach/upload
     * 1장 이상, 최대 5장까지의 이미지를 S3와 MariaDB에 업로드한다.
     *
     * @param request type, id, imgList
     * @return AttachResponse
     */
    @PostMapping("/upload")
    public ResponseEntity<AttachResponse> uploadImage(@Valid AttachRequest.UploadRequest request) {
        AttachResponse response = attachService.uploadImage(request);
        return ResponseEntity.ok().body(response);
    }

}
