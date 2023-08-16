package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.exception.ImageUploadOutOfRangeException;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
import com.kakao.saramaracommunity.common.dto.Payload;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kakao.saramaracommunity.attach.entity.AttachType.BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
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
    AttachRepository attachRepository;

    @Autowired
    private AttachService attachService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        attachRepository.deleteAllInBatch();
    }

    @DisplayName("1장의 이미지를 AWS S3 버킷에 등록한다.")
    @Test
    void singleImageUploadAWSS3Bucket() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8));
        imgList.add(file);

        AttachRequest.UploadBucketRequest request = AttachRequest.UploadBucketRequest.builder()
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadBucketResponse response = attachService.uploadS3BucketImages(request);

        System.out.println(response);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.");
        assertThat(response.getData()).hasSize(1);

        // 테스트 노란불 뜸 - 실제 파일명은 다음과 같음 "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/%2F%2Ftest_1690875139637.png"
        // assertThat(response.getData()).contains("test.png");

    }

    @DisplayName("1번 게시글에 대한 1장의 이미지 URL(S3 이미지 객체 URL)을 DB에 저장한다.")
    @Test
    void singleImageUpload() {
        // given
        Map<Long, String> imgList = new HashMap<>();
        String imgPath = "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_1.png";
        imgList.put(1L, imgPath);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 DB에 이미지 업로드를 완료했습니다.");
        assertThat(response.isData()).isTrue();

    }

    @DisplayName("5장의 이미지를 AWS S3 버킷에 등록한다.")
    @Test
    void MultipleImageUploadAWSS3Bucket() {
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

        AttachRequest.UploadBucketRequest request = AttachRequest.UploadBucketRequest.builder()
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadBucketResponse response = attachService.uploadS3BucketImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 AWS S3 버킷에 이미지 등록을 완료했습니다.");
        assertThat(response.getData()).hasSize(5);

    }

    @DisplayName("1번 게시글에 대해 5장의 이미지 URL(S3 이미지 객체 URL)을 DB에 저장한다.")
    @Test
    void MultipleImageUpload() {
        // given
        Map<Long, String> imgList = new HashMap<>();
        imgList.put(1L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_1.png");
        imgList.put(2L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_2.png");
        imgList.put(3L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_3.png");
        imgList.put(4L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_4.png");
        imgList.put(5L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_5.png");

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 DB에 이미지 업로드를 완료했습니다.");
        assertThat(response.isData()).isTrue();

    }

    @DisplayName("AWS S3 버킷에 1장 미만의 이미지를 업로드할 경우 예외가 발생한다.")
    @Test
    void ZeroUploadImageAWSS3Bucket() {
        // given
        List<MultipartFile> imgList = new ArrayList<>();

        AttachRequest.UploadBucketRequest request = AttachRequest.UploadBucketRequest.builder()
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> attachService.uploadS3BucketImages(request))
                .isInstanceOf(ImageUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    @DisplayName("DB에 이미지 객체 URL이 빈 이미지 업로드 요청을 할 경우 예외가 발생한다.")
    @Test
    void ZeroUploadImage() {
        // given
        Map<Long, String> imgList = new HashMap<>();

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> attachService.uploadImages(request))
                .isInstanceOf(ImageUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    @DisplayName("AWS S3 버킷에 6장의 이미지를 업로드할 경우 예외가 발행한다.")
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

        AttachRequest.UploadBucketRequest request = AttachRequest.UploadBucketRequest.builder()
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> attachService.uploadS3BucketImages(request))
                .isInstanceOf(ImageUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    @DisplayName("DB에는 6장의 이미지 객체 URL을 업로드할 경우 예외가 발생한다.")
    @Test
    void MoreThenFiveUploadImage() {
        // given
        Map<Long, String> imgList = new HashMap<>();
        imgList.put(1L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_1.png");
        imgList.put(2L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_2.png");
        imgList.put(3L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_3.png");
        imgList.put(4L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_4.png");
        imgList.put(5L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_5.png");
        imgList.put(6L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_6.png");

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> attachService.uploadImages(request))
                .isInstanceOf(ImageUploadOutOfRangeException.class)
                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");

    }

    @DisplayName("하나의 게시글에 등록된 이미지 목록을 조회한다.")
    @Test
    void getBoardImages() {

        // given
        Attach attach1 = createAttach(1L, 1L, "test1.jpg");
        Attach attach2 = createAttach(1L, 2L, "test2.jpg");
        Attach attach3 = createAttach(1L, 3L, "test3.jpg");
        attachRepository.saveAll(List.of(attach1, attach2, attach3));

        AttachRequest.GetBoardImageRequest request = AttachRequest.GetBoardImageRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .build();

        // when
        AttachResponse.GetImageResponse response = attachService.getBoardImages(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다.");
        assertThat(response.getData().values()).hasSize(3)
                .contains(
                        Map.of(1L, "test1.jpg"),
                        Map.of(2L, "test2.jpg"),
                        Map.of(3L, "test3.jpg")
                );

    }

    @DisplayName("모든 게시글에 등록된 이미지 목록을 조회한다.")
    @Test
    void getAllBoardImages() {

        // given

        // first board attachs
        Attach firstBoardAttach1 = createAttach(1L, 1L, "first1.jpg");
        Attach firstBoardAttach2 = createAttach(1L, 2L, "first2.jpg");
        Attach firstBoardAttach3 = createAttach(1L, 3L, "first3.jpg");

        // second board attachs
        Attach secondBoardAttach1 = createAttach(2L, 1L, "sec1.jpg");
        Attach secondBoardAttach2 = createAttach(2L, 2L, "sec2.jpg");
        Attach secondBoardAttach3 = createAttach(2L, 3L, "sec3.jpg");
        Attach secondBoardAttach4 = createAttach(2L, 4L, "sec4.jpg");

        // third board attachs
        Attach thirdBoardAttach1 = createAttach(3L, 1L, "third1.jpg");

        attachRepository.saveAll(
                List.of(
                        firstBoardAttach1,
                        firstBoardAttach2,
                        firstBoardAttach3,
                        secondBoardAttach1,
                        secondBoardAttach2,
                        secondBoardAttach3,
                        secondBoardAttach4,
                        thirdBoardAttach1
                )
        );

        // when
        AttachResponse.GetAllImageResponse response = attachService.getAllBoardImages();

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다.");
        assertThat(response.getData().keySet()).containsExactly(1L, 2L, 3L);
        assertThat(response.getData().keySet()).hasSize(3);

    }

    @DisplayName("하나의 게시글에 등록된 3개의 이미지 중 첫번째 이미지를 새로운 이미지로 수정한다.")
    @Test
    void updateImage() {
        // given
        Attach attach1 = createAttach(1L, 1L, "first1.jpg");
        Attach attach2 = createAttach(1L, 2L, "second2.jpg");
        Attach attach3 = createAttach(1L, 3L, "third3.jpg");
        attachRepository.saveAll(List.of(attach1, attach2, attach3));

        AttachRequest.UpdateRequest request = AttachRequest.UpdateRequest.builder()
                .attachId(attach1.getAttachId())
                .attachType(BOARD)
                .ids(1L)
                .imgPath("first_1.png")
                .build();

        // when
        AttachResponse.UpdateResponse response = attachService.updateImage(request);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMsg()).isEqualTo("정상적으로 게시글의 이미지를 수정했습니다.");
        assertThat(response.isData()).isTrue();
    }

    @DisplayName("하나의 게시글에 등록된 하나의 이미지를 삭제한다.")
    @Test
    void removeImage() {
        // given
        Attach attach1 = createAttach(1L, 1L, "first1.jpg");
        Attach attach2 = createAttach(1L, 2L, "second2.jpg");
        Attach attach3 = createAttach(1L, 3L, "third3.jpg");
        attachRepository.saveAll(List.of(attach1, attach2, attach3));

        // when
        AttachResponse.DeleteResponse response = attachService.deleteImage(attach1.getAttachId());

        // then
        assertThat(response.getCode()).isEqualTo(204);
        assertThat(response.getMsg()).isEqualTo("정상적으로 이미지를 삭제했습니다.");
        assertThat(response.isData()).isTrue();

    }

    private Attach createAttach(Long ids, Long seq, String path) {
        return Attach.builder()
                .type(BOARD)
                .ids(ids)
                .seq(seq)
                .imgPath(path)
                .build();
    }


}