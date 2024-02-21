package com.kakao.saramaracommunity.bucket.controller;

import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.containsString;
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

    @Nested
    @DisplayName("AWS S3 버킷에")
    class AWS_S3_버킷에 {
        @Test
        @DisplayName("이미지 3장을 업로드할 수 있다.")
        void 이미지_3장을_등록할_수_있다() throws Exception {
            // given
            List<MockMultipartFile> request = createImageFileList(3);

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
                    .andExpect(jsonPath("$.message").value("성공적으로 AWS S3 버킷에 이미지 업로드를 완료하였습니다."));
        }
        @Test
        @DisplayName("이미지 파일 목록을 요청으로 보내지 않는다면 예외가 발생한다.")
        void 요청한_이미지_파일_목록이_비어있다면_예외가_발생한다() throws Exception {
            // when & then
            mockMvc.perform(
                            multipart("/api/v1/bucket")
                                    .with(csrf())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value(containsString("요청으로 받은 이미지 목록 필드가 누락되었습니다.")));
        }

        @Test
        @DisplayName("이미지 파일 목록이 5장을 초과한다면 예외가 발생한다.")
        void 이미지_파일_목록이_5장을_초과한다면_예외가_발생한다() throws Exception {
            // given
            List<MockMultipartFile> request = createImageFileList(6);

            // when & then
            mockMvc.perform(
                            multipart("/api/v1/bucket")
                                    .file(request.get(0))
                                    .file(request.get(1))
                                    .file(request.get(2))
                                    .file(request.get(3))
                                    .file(request.get(4))
                                    .file(request.get(5))
                                    .with(csrf())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value(containsString("이미지는 최대 5장까지만 업로드할 수 있습니다.")));
        }
    }

    private static List<MockMultipartFile> createImageFileList(int size) {
        return IntStream.range(0, size)
                .mapToObj(idx -> new MockMultipartFile("request", "test"+idx+".png", "image/png", "test_file".getBytes(UTF_8)))
                .collect(Collectors.toList());
    }

}
