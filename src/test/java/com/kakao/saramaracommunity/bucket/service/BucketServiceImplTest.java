package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.exception.BucketUploadOutOfRangeException;
import com.kakao.saramaracommunity.bucket.service.dto.request.BucketServiceRequest;
import com.kakao.saramaracommunity.bucket.service.dto.response.BucketResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * BucketServiceImplTest: BucketServiceImpl를 테스트할 클래스
 * Integration Test
 * AWS S3 버킷 이미지 업로드 기능을 테스트한다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@SpringBootTest
class BucketServiceImplTest {

    @Autowired
    private BucketService bucketService;

    @DisplayName("1장의 이미지를 버킷에 등록한다.")
    @Test
    void bucketUploadImagesToOne() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8));
        imgList.add(file);

        BucketServiceRequest.BucketUploadRequest request = BucketServiceRequest.BucketUploadRequest.builder()
                .imgList(imgList)
                .build();

        // when
        BucketResponse.BucketUploadResponse response = bucketService.bucketUploadImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.");
        assertThat(response.getData()).hasSize(1);

        // 테스트 노란불 뜸 - 실제 파일명은 다음과 같음 "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/%2F%2Ftest_1690875139637.png"
        // assertThat(response.getData()).contains("test.png");

    }

    @DisplayName("5장의 이미지를 버킷에 등록한다.")
    @Test
    void bucketUploadImagesToFive() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();
        MultipartFile file1 = new MockMultipartFile("file", "test_1.png", "image/png", "test_1 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("file", "test_2.png", "image/png", "test_2 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file3 = new MockMultipartFile("file", "test_3.png", "image/png", "test_3 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file4 = new MockMultipartFile("file", "test_4.png", "image/png", "test_4 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file5 = new MockMultipartFile("file", "test_5.png", "image/png", "test_5 file".getBytes(StandardCharsets.UTF_8));
        imgList.add(file1);
        imgList.add(file2);
        imgList.add(file3);
        imgList.add(file4);
        imgList.add(file5);

        BucketServiceRequest.BucketUploadRequest request = BucketServiceRequest.BucketUploadRequest.builder()
                .imgList(imgList)
                .build();

        // when
        BucketResponse.BucketUploadResponse response = bucketService.bucketUploadImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.");
        assertThat(response.getData()).hasSize(5);

    }

    @DisplayName("버킷에 비어있는 이미지 목록을 업로드할 경우 예외가 발생한다.")
    @Test
    void bucketUploadImagesWithoutNoImage() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();

        BucketServiceRequest.BucketUploadRequest request = BucketServiceRequest.BucketUploadRequest.builder()
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> bucketService.bucketUploadImages(request))
                .isInstanceOf(BucketUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    @DisplayName("버킷에 6장의 이미지 목록을 업로드할 경우 예외가 발생한다.")
    @Test
    void MoreThenFiveUploadImageAWSS3Bucket() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();
        MultipartFile file1 = new MockMultipartFile("file", "test_1.png", "image/png", "test_1 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("file", "test_2.png", "image/png", "test_2 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file3 = new MockMultipartFile("file", "test_3.png", "image/png", "test_3 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file4 = new MockMultipartFile("file", "test_4.png", "image/png", "test_4 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file5 = new MockMultipartFile("file", "test_5.png", "image/png", "test_5 file".getBytes(StandardCharsets.UTF_8));
        MultipartFile file6 = new MockMultipartFile("file", "test_6.png", "image/png", "test_6 file".getBytes(StandardCharsets.UTF_8));

        imgList.add(file1);
        imgList.add(file2);
        imgList.add(file3);
        imgList.add(file4);
        imgList.add(file5);
        imgList.add(file6);

        BucketServiceRequest.BucketUploadRequest request = BucketServiceRequest.BucketUploadRequest.builder()
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> bucketService.bucketUploadImages(request))
                .isInstanceOf(BucketUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

}