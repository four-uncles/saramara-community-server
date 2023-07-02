package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.config.AwsS3MockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.kakao.saramaracommunity.attach.entity.AttachType.BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * AttachServiceImplTest: 이미지 업로드 서비스 구현체를 테스트할 클래스
 *
 * 아직 단위 테스트와 통합 테스트 구분이 명확하지 않은 상태입니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@SpringBootTest
class AttachServiceImplTest {

    @Autowired
    private AttachService attachService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("1장의 이미지를 업로드하면 S3와 DB에 이미지가 업로드된다.")
    @Test
    void singleUploadImage() {
        // given
        Map<Long, MultipartFile> imgList = new HashMap<>();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8));
        imgList.put(1L, file);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .type(BOARD)
                .id(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.isData()).isTrue();

    }

    @DisplayName("여러 장의 이미지를 업로드하면 S3와 DB에 이미지가 업로드된다.")
    @Test
    void MultipleUploadImage() {
        // given
        Map<Long, MultipartFile> imgList = new HashMap<>();
        MultipartFile file1 = new MockMultipartFile("file", "test_1.png", "image/png", "test_1 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("file", "test_2.png", "image/png", "test_2 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file3 = new MockMultipartFile("file", "test_3.png", "image/png", "test_3 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file4 = new MockMultipartFile("file", "test_4.png", "image/png", "test_4 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file5 = new MockMultipartFile("file", "test_5.png", "image/png", "test_5 file".getBytes(StandardCharsets.UTF_8));
        imgList.put(1L, file1);
        imgList.put(2L, file2);
        imgList.put(3L, file3);
        imgList.put(4L, file4);
        imgList.put(5L, file5);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .type(BOARD)
                .id(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.isData()).isTrue();

    }

    @DisplayName("이미지는 최소 1장이상 업로드할 수 있다.")
    @Test
    void ZeroUploadImage() {
        // given
        Map<Long, MultipartFile> imgList = new HashMap<>();

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .type(BOARD)
                .id(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.isData()).isFalse();

    }

    @DisplayName("이미지는 최대 5장까지 업로드할 수 있다.")
    @Test
    void MoreThenFiveUploadImage() {
        // given
        Map<Long, MultipartFile> imgList = new HashMap<>();
        MultipartFile file1 = new MockMultipartFile("file", "test_1.png", "image/png", "test_1 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("file", "test_2.png", "image/png", "test_2 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file3 = new MockMultipartFile("file", "test_3.png", "image/png", "test_3 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file4 = new MockMultipartFile("file", "test_4.png", "image/png", "test_4 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file5 = new MockMultipartFile("file", "test_5.png", "image/png", "test_5 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file6 = new MockMultipartFile("file", "test_6.png", "image/png", "test_6 file".getBytes(StandardCharsets.UTF_8));
        imgList.put(1L, file1);
        imgList.put(2L, file2);
        imgList.put(3L, file3);
        imgList.put(4L, file4);
        imgList.put(5L, file5);
        imgList.put(6L, file6);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .type(BOARD)
                .id(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.isData()).isFalse();

    }

}