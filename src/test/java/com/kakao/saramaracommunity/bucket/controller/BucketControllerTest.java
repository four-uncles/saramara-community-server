package com.kakao.saramaracommunity.bucket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.bucket.controller.dto.request.BucketRequest;
import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.bucket.service.BucketService;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketResponse;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BucketControllerTest: Presentaion Layer인 BucketController를 테스트할 클래스
 * @WebMvcTest를 통해 웹 계층을 슬라이스 테스트로 진행한다.
 *
 * List<MultipartFile> 타입 필드의 경우 objectMapper를 통한 직렬화가 불가하기에 Mock을 이용해 Stubbing하여 테스트를 진행했다.
 *
 * @author Taejun
 * @version 0.0.1
 */
class BucketControllerTest extends ControllerTestSupport {

    @DisplayName("AWS S3 버킷에 새로운 이미지 목록을 등록한다.")
    @Test
    void bucketUploadImages() throws Exception {
        // given
        BucketRequest.BucketUploadRequest request = BucketRequest.BucketUploadRequest.builder()
        .imgList(new ArrayList<>())
        .build();

        given(bucketService.bucketUploadImages(any()))
                .willReturn(BucketResponse.BucketUploadResponse.of(
                        200,
                        "정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.",
                        List.of(
                                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"
                        )
                ));

        // when & then
        mockMvc.perform(
                        post("/api/v1/bucket/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다."))
                .andExpect(jsonPath("$.data").isArray());

    }

    @DisplayName("AWS S3 버킷에 새로운 이미지 목록을 등록할 때, 이미지 파일 목록은 필수로 입력되어야 한다.")
    @Test
    void bucketUploadImagesWithoutImgList() throws Exception {
        // given
        BucketRequest.BucketUploadRequest request = BucketRequest.BucketUploadRequest.builder()
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/v1/bucket/upload")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message", containsString("이미지 목록이 비어있습니다.")));

    }

}