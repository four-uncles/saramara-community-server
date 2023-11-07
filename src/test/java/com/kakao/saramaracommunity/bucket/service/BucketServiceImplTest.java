package com.kakao.saramaracommunity.bucket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketCreateResponse;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * AWS S3 버킷 이미지 업로드 기능이 존재하는 BucketServiceImpl를 테스트할 클래스입니다.
 * 통합 테스트를 위해 @SpringBootTest를 설정했습니다.
 */
class BucketServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() throws Exception {
        given(amazonS3.putObject(any(PutObjectRequest.class))).willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any())).willReturn(new URL("https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"));
    }

    @DisplayName("1장의 이미지를 버킷에 등록한다.")
    @Test
    void uploadImagesToOne() {
        // given
        List<MultipartFile> request = createImageFileList(1);

        // when
        BucketCreateResponse response = bucketService.uploadImages(request);

        // then
        assertThat(response.getImages()).hasSize(1)
                .isEqualTo(List.of(
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"
                ));
    }

    @DisplayName("5장의 이미지를 버킷에 등록한다.")
    @Test
    void uploadImagesToFive() {
        // given
        List<MultipartFile> request = createImageFileList(5);

        // when
        BucketCreateResponse response = bucketService.uploadImages(request);

        // then
        assertThat(response.getImages()).hasSize(5)
                .isEqualTo(List.of(
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                        "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"
                ));
    }

    @DisplayName("버킷에 비어있는 이미지 목록을 업로드할 경우 예외가 발생한다.")
    @Test
    void uploadImagesWithoutNoImage() {
        // given
        List<MultipartFile> request = createImageFileList(0);

        // when & then
        assertThatThrownBy(() -> bucketService.uploadImages(request))
                .isInstanceOf(BucketUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");
    }

    @DisplayName("버킷에 6장의 이미지 목록을 업로드할 경우 예외가 발생한다.")
    @Test
    void MoreThenFiveUploadImages() {
        // given
        List<MultipartFile> request = createImageFileList(6);

        // when & then
        assertThatThrownBy(() -> bucketService.uploadImages(request))
                .isInstanceOf(BucketUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    private List<MultipartFile> createImageFileList(int size) {
        List<MultipartFile> imageFileList = new ArrayList<>();
        for(int i=0; i<size; i++) {
            imageFileList.add(new MockMultipartFile("test", "test.png", "image/png", "test_file".getBytes(StandardCharsets.UTF_8)));
        }
        return imageFileList;
    }

}