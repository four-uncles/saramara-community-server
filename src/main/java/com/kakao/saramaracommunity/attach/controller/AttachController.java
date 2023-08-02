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
     * uploadImageDeprecated: 이미지 업로드 API
     * URL: POST /api/v1/attach/upload
     * 1장 이상, 최대 5장까지의 이미지를 S3와 MariaDB에 업로드한다.
     *
     * @param request type, id, imgList
     * @return AttachResponse.UploadResponse
     */
    @PostMapping("/upload/deprecated")
    public ResponseEntity<AttachResponse.UploadResponse> uploadImageDeprecated(@RequestBody @Valid AttachRequest.UploadRequestDeprecated request) {
        AttachResponse.UploadResponse response = attachService.uploadImageDeprecated(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * uploadS3BucketImages: AWS S3 Bucket 이미지 파일 등록 API
     * URL: POST /api/v1/attach/upload/bucket
     *
     * @param request type, id, imgList
     * @return AttachResponse.UploadBucketResponse
     */
    @PostMapping("/upload/bucket")
    public ResponseEntity<AttachResponse.UploadBucketResponse> uploadS3BucketImages(@RequestBody @Valid AttachRequest.UploadBucketRequest request) {
        AttachResponse.UploadBucketResponse response = attachService.uploadS3BucketImages(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * uploadImages: AWS S3 Bucket 이미지 파일 등록 API
     * URL: POST /api/v1/attach/upload/bucket
     *
     * @param request type, id, imgList
     * @return AttachResponse.UploadBucketResponse
     */
    @PostMapping("/upload")
    public ResponseEntity<AttachResponse.UploadResponse> uploadImages(@RequestBody @Valid AttachRequest.UploadRequest request) {
        AttachResponse.UploadResponse response = attachService.uploadImages(request);
        return ResponseEntity.ok().body(response);
    }


    /**
     * getImage: 게시글의 등록된 이미지 조회 API
     * URL: GET /api/v1/attach/board
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

    /**
     * getAllBoardImages: 모든 게시글의 등록된 이미지 조회 API
     * URL: GET /api/v1/attach/boards
     * 모든 게시글의 등록된 S3 이미지 주소(URL)를 게시글: 이미지목록(Map) 형태로 응답한다.
     *
     * @return AttachResponse.GetAllImageResponse
     */
    @GetMapping("/boards")
    public ResponseEntity<AttachResponse.GetAllImageResponse> getAllBoardImages() {
        AttachResponse.GetAllImageResponse response = attachService.getAllBoardImages();
        return ResponseEntity.ok().body(response);
    }

    /**
     * updateImage: 게시글의 등록된 하나의 이미지 수정 API
     * URL: PUT /api/v1/attach/board
     *
     * @return AttachResponse.UpdateResponse
     */
    @PutMapping("/board")
    public ResponseEntity<AttachResponse.UpdateResponse> updateImage(@RequestBody @Valid AttachRequest.UpdateRequest request) {
        AttachResponse.UpdateResponse response = attachService.updateImage(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * deleteImage: 게시글의 등록된 하나의 이미지 삭제 API
     * URL: DELETE /api/v1/attach/board
     *
     * @return AttachResponse.DeleteResponse
     */
    @DeleteMapping("/board")
    public ResponseEntity<AttachResponse.DeleteResponse> deleteImage(@Valid @PathVariable Long attachId) {
        AttachResponse.DeleteResponse response = attachService.deleteImage(attachId);
        return ResponseEntity.ok().body(response);
    }


}
