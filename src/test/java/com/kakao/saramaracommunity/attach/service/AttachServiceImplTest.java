package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.service.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.exception.ImageUploadOutOfRangeException;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
import com.kakao.saramaracommunity.attach.service.dto.request.AttachServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kakao.saramaracommunity.attach.entity.AttachType.BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * AttachServiceImplTest: AttachService의 구현체를 테스트할 클래스
 * Integration Test
 * Attach 테이블과 상호작용하는 기능을 테스트한다.
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

    @DisplayName("1번 게시글에 대한 1장의 이미지 URL(S3 이미지 객체 URL)을 DB에 저장한다.")
    @Test
    void singleImageUpload() {
        // given
        Map<Long, String> imgList = new HashMap<>();
        String imgPath = "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_1.png";
        imgList.put(1L, imgPath);

        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
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

        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
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

    @DisplayName("DB에 이미지 객체 URL이 빈 이미지 업로드 요청을 할 경우 예외가 발생한다.")
    @Test
    void ZeroUploadImage() {
        // given
        Map<Long, String> imgList = new HashMap<>();

        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when & then
        assertThatThrownBy(() -> attachService.uploadImages(request))
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

        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
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

        AttachServiceRequest.GetBoardImageRequest request = AttachServiceRequest.GetBoardImageRequest.builder()
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

        AttachServiceRequest.UpdateRequest request = AttachServiceRequest.UpdateRequest.builder()
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