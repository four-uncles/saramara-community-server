package com.kakao.saramaracommunity.attach.controller;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.service.AttachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
     * @return AttachResponse.UploadResponse
     */
    @PostMapping("/upload")
    public ResponseEntity<AttachResponse.UploadResponse> uploadImage(@RequestBody @Valid AttachRequest.UploadRequest request) {
        AttachResponse.UploadResponse response = attachService.uploadImage(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * getImage: 게시글의 등록된 이미지 조회 API
     * URL: /api/v1/attach/board
     * 하나의 게시글에 등록된 S3 이미지 주소(URL)를 Map 형태로 응답한다.
     *
     * @param request attachType, attachId
     * @return AttachResponse.GetImageResponse
     */
    @GetMapping("/board")
    public ResponseEntity<AttachResponse.GetImageResponse> getBoardImages(@RequestBody @Valid AttachRequest.GetBoardImageRequest request) {
        AttachResponse.GetImageResponse response = attachService.getBoardImages(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/boards")
    public ResponseEntity<AttachResponse.GetAllImageResponse> getAllBoardImages() {
        AttachResponse.GetAllImageResponse response = attachService.getAllBoardImages();
        return ResponseEntity.ok().body(response);
    }

}
