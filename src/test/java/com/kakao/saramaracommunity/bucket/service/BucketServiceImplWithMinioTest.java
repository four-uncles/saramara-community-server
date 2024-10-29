package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.controller.port.BucketService;
import com.kakao.saramaracommunity.bucket.dto.business.response.BucketUploadResponse;
import com.kakao.saramaracommunity.bucket.exception.BucketBusinessException;
import com.kakao.saramaracommunity.bucket.service.support.FakeAwsS3Uploader;
import com.kakao.saramaracommunity.bucket.service.support.FakeMinioUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MAX_RANGE_OUT;
import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MIN_RANGE_OUT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

/**
 * 외부 오브젝트 스토리지와의 통신 여부를 검증할 소형 테스트 클래스입니다.
 * 여기서는 AWS S3 의존성을 검증합니다.
 */
class BucketServiceImplWithMinioTest {

    private BucketService bucketService;

    @BeforeEach
    void setUp() {
        this.bucketService = new BucketServiceImpl(new FakeMinioUploader());
        setField(bucketService, "MAX_IMAGE_COUNT", 5);
    }

    @Nested
    @DisplayName("이미지를 버킷에 업로드할 경우")
    class 이미지를_버킷에_업로드할_경우 {
        @Test
        @DisplayName("[Green] 이미지 URL 경로가 담긴 목록을 응답받는다.")
        void 이미지_URL_경로를_응답받는다() {
            // given
            List<MultipartFile> request = createImageFileList(1);

            // when
            BucketUploadResponse response = bucketService.uploadImages(request);

            // then
            assertThat(response.images())
                    .hasSize(1)
                    .allMatch(url -> url.contains("test.png"));
        }
        @Test
        @DisplayName("[Green] 여러 장을 업로드하면 복수의 이미지 URL 경로가 담긴 목록을 응답받는다.")
        void 여러_장을_업로드하면_복수의_이미지_URL_경로가_담긴_목록을_응답받는다() {
            // given
            List<MultipartFile> request = createImageFileList(3);

            // when
            BucketUploadResponse response = bucketService.uploadImages(request);

            // then
            assertThat(response.images())
                    .hasSize(3)
                    .allMatch(url -> url.contains("test.png"));
        }
        @Test
        @DisplayName("[Edge] 최소 1장의 이미지를 업로드해야 한다.")
        void 최소_1장의_이미지를_업로드해야_한다() {
            // given
            List<MultipartFile> request = createImageFileList(1);

            // when
            BucketUploadResponse response = bucketService.uploadImages(request);

            // then
            assertThat(response.images()).hasSize(1);
        }
        @Test
        @DisplayName("[Exception] 요청받은 이미지 목록이 비어있다면 예외가 발생한다.")
        void 요청받은_이미지_목록이_비어있다면_예외가_발생한다() {
            // given
            List<MultipartFile> request = createImageFileList(0);

            // when & then
            assertThatThrownBy(() -> bucketService.uploadImages(request))
                    .isInstanceOf(BucketBusinessException.class)
                    .hasMessage(BUCKET_IMAGE_MIN_RANGE_OUT.getMessage());
        }
        @Test
        @DisplayName("[Green] 최대 5장까지 업로드할 수 있다.")
        void 최대_5장까지_업로드할_수_있다() {
            // given
            List<MultipartFile> request = createImageFileList(5);

            // when
            BucketUploadResponse response = bucketService.uploadImages(request);

            // then
            assertThat(response.images()).hasSize(5);
        }
        @Test
        @DisplayName("[Exception] 요청받은 이미지가 5장을 초과한다면 예외가 발생한다.")
        void 요청받은_이미지가_5장을_초과한다면_예외가_발생한다() {
            // given
            List<MultipartFile> request = createImageFileList(6);

            // when & then
            assertThatThrownBy(() -> bucketService.uploadImages(request))
                    .isInstanceOf(BucketBusinessException.class)
                    .hasMessage(BUCKET_IMAGE_MAX_RANGE_OUT.getMessage());
        }
    }

    private static List<MultipartFile> createImageFileList(int size) {
        return IntStream.range(0, size)
            .mapToObj(i -> new MockMultipartFile("test", "test.png", "image/png", "test_file".getBytes(UTF_8)))
            .collect(Collectors.toList());
    }

}
