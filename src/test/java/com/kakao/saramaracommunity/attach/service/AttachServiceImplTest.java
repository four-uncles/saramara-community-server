package com.kakao.saramaracommunity.attach.service;

import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
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
import java.util.List;
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

    @DisplayName("1장의 이미지를 업로드하면 S3와 DB에 이미지가 업로드된다.")
    @Test
    void singleUploadImage() {
        // given
        Map<Long, MultipartFile> imgList = new HashMap<>();
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8));
        imgList.put(1L, file);

        AttachRequest.UploadRequest request = AttachRequest.UploadRequest.builder()
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImage(request);

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
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImage(request);

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
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("400 BAD_REQUEST");
        assertThat(response.getMsg()).isEqualTo("이미지는 1장 이상, 최대 5장까지 등록할 수 있습니다.");
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
                .attachType(BOARD)
                .ids(1L)
                .imgList(imgList)
                .build();

        // when
        AttachResponse.UploadResponse response = attachService.uploadImage(request);

        // then
        assertThat(response.getCode()).isEqualTo("400 BAD_REQUEST");
        assertThat(response.getMsg()).isEqualTo("이미지는 1장 이상, 최대 5장까지 등록할 수 있습니다.");
        assertThat(response.isData()).isFalse();

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
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.getMsg()).isEqualTo("정상적으로 해당 게시글의 등록된 이미지 목록을 조회하였습니다.");
        assertThat(response.getData()).hasSize(3);
        assertThat(response.getData().keySet()).containsExactly(1L, 2L, 3L);
        assertThat(response.getData().values()).containsExactly("test1.jpg", "test2.jpg", "test3.jpg");

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
        assertThat(response.getCode()).isEqualTo("200 OK");
        assertThat(response.getMsg()).isEqualTo("정상적으로 모든 게시글의 등록된 이미지 목록을 조회하였습니다.");
        assertThat(response.getData().keySet()).containsExactly(1L, 2L, 3L);
        assertThat(response.getData().keySet()).hasSize(3);

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