package com.kakao.saramaracommunity.attach.controller;

import com.kakao.saramaracommunity.attach.controller.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.exception.ImageUploadOutOfRangeException;
import com.kakao.saramaracommunity.attach.service.dto.response.AttachResponse;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.kakao.saramaracommunity.attach.entity.AttachType.BOARD;
import static com.kakao.saramaracommunity.attach.exception.AttachErrorCode.ATTACH_IMAGE_RANGE_OUT;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AttachControllerTest: Presentation Layer인 AttachController를 테스트할 클래스
 * @WebMvcTest를 통해 웹 계층을 슬라이스 테스트로 진행한다.
 *
 * @author Taejun
 * @version 0.0.1
 */
class AttachControllerTest extends ControllerTestSupport {

    @DisplayName("새로운 이미지 목록을 등록한다.")
    @Test
    void uploadImages() throws Exception {
        // given
        Map<Long, String> images = createImageMap(3);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(images)
                .build();

        given(attachService.uploadImages(any()))
                .willReturn(AttachResponse.UploadResponse.builder()
                        .code(200)
                        .msg("정상적으로 DB에 이미지 업로드를 완료했습니다.")
                        .data(true)
                        .build()
        );

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("정상적으로 DB에 이미지 업로드를 완료했습니다."))
                .andExpect(jsonPath("$.data").isBoolean());
    }

    @DisplayName("새로운 이미지 목록을 등록할 때, 첨부파일의 유형은 필수로 입력되어야 한다.")
    @Test
    void uploadImagesWithoutAttachType() throws Exception {
        // given
        Map<Long, String> images = createImageMap(0);
        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .ids(1L)
                .imgList(images)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", containsString("어떤 유형의 글에서 이미지가 등록되었는지 알 수 없습니다.")));
    }

    @DisplayName("새로운 이미지 목록을 등록할 때, 게시글 번호는 필수로 입력되어야 한다.")
    @Test
    void uploadImagesWithoutBoardId() throws Exception {
        // given
        Map<Long, String> images = createImageMap(0);
        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .imgList(images)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", containsString("게시글이나 댓글의 번호값을 알 수 없습니다.")));
    }

    @DisplayName("새로운 이미지 목록을 등록할 때, 이미지 목록은 필수로 입력되어야 한다.")
    @Test
    void uploadImagesWithoutImgList() throws Exception {
        // given
        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", containsString("이미지 목록이 비어있습니다.")));
    }

    @DisplayName("새로운 이미지 목록을 등록할 때, 이미지는 1장 이상이어야 한다.")
    @Test
    void uploadImagesImageListIsEmpty() throws Exception {
        // given
        Map<Long, String> imgList = createImageMap(0);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        given(attachService.uploadImages(any()))
                .willThrow(new ImageUploadOutOfRangeException(ATTACH_IMAGE_RANGE_OUT));

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ATTACH_IMAGE_RANGE_OUT.getHttpStatus().value()))
                .andExpect(jsonPath("$.message").value(ATTACH_IMAGE_RANGE_OUT.getMessage()));
    }

    @DisplayName("새로운 이미지 목록을 등록할 때, 이미지가 6장 이상일 경우 예외가 발생한다.")
    @Test
    void uploadImagesImageListIsRangeOut() throws Exception {
        // given
        Map<Long, String> imgList = createImageMap(6);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        given(attachService.uploadImages(any()))
                .willThrow(new ImageUploadOutOfRangeException(ATTACH_IMAGE_RANGE_OUT));

        // when & then
        mockMvc.perform(
                        post("/api/v1/attach/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ATTACH_IMAGE_RANGE_OUT.getHttpStatus().value()))
                .andExpect(jsonPath("$.message").value(ATTACH_IMAGE_RANGE_OUT.getMessage()));
    }

    @DisplayName("특정 게시글의 이미지 목록을 조회한다.")
    @Test
    void getBoardImages() throws Exception {
        // given
        AttachRequest.GetBoardImageRequest request = AttachRequest.GetBoardImageRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .build();

        Map<Long, Map<Long, String>> result = Map.of();
        given(attachService.getBoardImages(any()))
                .willReturn(AttachResponse.GetImageResponse.builder()
                        .code(200)
                        .msg("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다.")
                        .data(result)
                        .build());

        // when & then
        mockMvc.perform(
                        get("/api/v1/attach/board")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다."))
                .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("특정 게시글의 이미지 목록을 조회할 때, 게시글번호는 필수로 입력되어야 한다.")
    @Test
    void getBoardImagesWithoutBoardId() throws Exception {
        // given
        AttachRequest.GetBoardImageRequest request = AttachRequest.GetBoardImageRequest.builder()
                .attachType(BOARD)
                .build();

        // when & then
        mockMvc.perform(
                        get("/api/v1/attach/board")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", containsString("게시글이나 댓글의 번호값을 알 수 없습니다.")));
    }

    @DisplayName("모든 게시글의 이미지 목록들을 조회한다.")
    @Test
    void getAllBoardImages() throws Exception {
        // given
        Map<Long, Map<Long, String>> result = Map.of();
        given(attachService.getAllBoardImages())
                .willReturn(AttachResponse.GetAllImageResponse.builder()
                        .code(200)
                        .msg("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다.")
                        .data(result)
                        .build());

        // when & then
        mockMvc.perform(
                        get("/api/v1/attach/boards")
                                .contentType(APPLICATION_JSON)
                                .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다."))
                .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("게시글의 이미지 목록에서 하나의 이미지를 변경한다.")
    @Test
    void updateImage() throws Exception {
        // given
        AttachRequest.UpdateRequest request = AttachRequest.UpdateRequest.builder()
                .attachType(BOARD)
                .attachId(1L)
                .ids(1L)
                .imgPath("https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png")
                .build();

        given(attachService.updateImage(any()))
                .willReturn(AttachResponse.UpdateResponse.builder()
                        .code(200)
                        .msg("정상적으로 게시글의 이미지를 수정했습니다.")
                        .data(true)
                        .build());

        // when & then
        mockMvc.perform(
                        put("/api/v1/attach/board")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("정상적으로 게시글의 이미지를 수정했습니다."))
                .andExpect(jsonPath("$.data").isBoolean());
    }

    @DisplayName("게시글의 이미지 목록에서 하나의 이미지를 삭제한다.")
    @Test
    void deleteImage() throws Exception {
        // given
        given(attachService.deleteImage(any()))
                .willReturn(AttachResponse.DeleteResponse.builder()
                        .code(204)
                        .msg("정상적으로 이미지를 삭제했습니다.")
                        .data(true)
                        .build());

        // when & then
        mockMvc.perform(
                        delete("/api/v1/attach/board/1")
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.code").value("204"))
                .andExpect(jsonPath("$.msg").value("정상적으로 이미지를 삭제했습니다."))
                .andExpect(jsonPath("$.data").isBoolean());
    }

    /**
     * 요청에 필요한 이미지 목록을 생성하는 메서드
     * @param size
     */
    private Map<Long, String> createImageMap(int size) {
        Map<Long, String> images = new HashMap<>();
        for(int i=1; i<=size; i++) {
            Long seq = (long) i;
            images.put(seq, "test" + i + ".jpg");
        }
        return images;
    }

}