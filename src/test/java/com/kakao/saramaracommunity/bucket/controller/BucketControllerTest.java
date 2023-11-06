package com.kakao.saramaracommunity.bucket.controller;

import com.kakao.saramaracommunity.bucket.service.dto.response.BucketCreateResponse;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Presentaion Layer인 BucketController를 테스트할 클래스입니다.
 * 웹 계층을 슬라이스 테스트를 위해 @WebMvcTest를 설정했습니다.
 * List<MultipartFile> 타입 필드의 경우 objectMapper를 통한 직렬화가 불가하기에 Mock을 이용해 Stubbing하여 테스트를 진행했습니다.
 */
class BucketControllerTest extends ControllerTestSupport {

    @DisplayName("AWS S3 버킷에 새로운 이미지 목록을 등록한다.")
    @Test
    void uploadImages() throws Exception {
        // given
        List<MockMultipartFile> request = List.of(
                new MockMultipartFile("request", "image1.png", "image/png", "image1".getBytes()),
                new MockMultipartFile("request", "image2.png", "image/png", "image2".getBytes()),
                new MockMultipartFile("request", "image3.png", "image/png", "image3".getBytes())
        );

        List<String> expectedUrls = List.of(
                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/image1.png",
                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/image2.png",
                "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/image3.png"
        );

        given(bucketService.uploadImages(any()))
                .willReturn(BucketCreateResponse.of(expectedUrls));

        // when & then
        mockMvc.perform(
                        multipart("/api/v1/bucket")
                                .file(request.get(0))
                                .file(request.get(1))
                                .file(request.get(2))
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.images").isArray());
    }

}