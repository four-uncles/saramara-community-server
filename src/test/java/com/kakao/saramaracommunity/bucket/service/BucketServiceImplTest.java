package com.kakao.saramaracommunity.bucket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.kakao.saramaracommunity.bucket.dto.business.response.BucketUploadResponse;
import com.kakao.saramaracommunity.bucket.exception.BucketBusinessException;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MAX_RANGE_OUT;
import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MIN_RANGE_OUT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * AWS S3 버킷 이미지 업로드 기능이 존재하는 BucketServiceImpl를 테스트할 클래스입니다.
 * 통합 테스트를 위해 @SpringBootTest를 설정했습니다.
 * 추후, 통합 테스트가 아닌 Mockking을 통한 단위 테스트로 수정할 예정입니다.
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
            assertThat(response.images()).hasSize(1)
                    .isEqualTo(List.of(
                            "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"
                    ));
        }
        @Test
        @DisplayName("[Green] 여러 장을 업로드하면 복수의 이미지 URL 경로가 담긴 목록을 응답받는다.")
        void 여러_장을_업로드하면_복수의_이미지_URL_경로가_담긴_목록을_응답받는다() {
            // given
            List<MultipartFile> request = createImageFileList(3);

            // when
            BucketUploadResponse response = bucketService.uploadImages(request);

            // then
            assertThat(response.images()).hasSize(3)
                    .isEqualTo(List.of(
                            "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                            "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png",
                            "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png"
                    ));
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
