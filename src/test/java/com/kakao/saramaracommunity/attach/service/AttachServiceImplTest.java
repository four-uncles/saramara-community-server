//package com.kakao.saramaracommunity.attach.service;
//
//import com.kakao.saramaracommunity.attach.entity.Attach;
//import com.kakao.saramaracommunity.attach.exception.AttachNotFoundException;
//import com.kakao.saramaracommunity.attach.repository.AttachRepository;
//import com.kakao.saramaracommunity.attach.service.dto.request.board.AttachServiceBoardCreateRequest;
//import com.kakao.saramaracommunity.attach.service.dto.response.board.AttachBoardCreateResponse;
//import com.kakao.saramaracommunity.attach.service.dto.response.board.AttachBoardGetAllResponse;
//import com.kakao.saramaracommunity.attach.service.dto.response.board.AttachBoardGetResponse;
//import com.kakao.saramaracommunity.board.entity.AttachBoard;
//import com.kakao.saramaracommunity.attachBoard.repository.AttachBoardRepository;
//import com.kakao.saramaracommunity.board.entity.Board;
//import com.kakao.saramaracommunity.board.entity.CategoryBoard;
//import com.kakao.saramaracommunity.board.exception.BoardNotFoundException;
//import com.kakao.saramaracommunity.board.repository.BoardRepository;
//import com.kakao.saramaracommunity.member.entity.Member;
//import com.kakao.saramaracommunity.member.entity.Type;
//import com.kakao.saramaracommunity.member.repository.MemberRepository;
//import com.kakao.saramaracommunity.support.IntegrationTestSupport;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
///**
// * Business Layer인 AttachService의 구현체를 테스트할 테스트 클래스입니다.
// * 통합 테스트를 위해 @SpringBootTest를 설정했습니다.
// */
//class AttachServiceImplTest extends IntegrationTestSupport {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private AttachRepository attachRepository;
//
//    @Autowired
//    private AttachBoardRepository attachBoardRepository;
//
//    @Autowired
//    private AttachService attachService;
//
//    @AfterEach
//    void tearDown() {
//        attachBoardRepository.deleteAllInBatch();
//        attachRepository.deleteAllInBatch();
//        boardRepository.deleteAllInBatch();
//        memberRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("하나의 게시글에 등록된 이미지 목록을 조회한다.")
//    @Test
//    void getBoardImages() {
//        // given
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Board board = createBoard(member);
//        Board savedBoard = boardRepository.save(board);
//
//        Attach attach1 = createAttach("test1.jpg");
//        Attach attach2 = createAttach("test2.jpg");
//        Attach attach3 = createAttach("test3.jpg");
//        attachRepository.saveAll(List.of(attach1, attach2, attach3));
//
//        AttachBoard attachBoard1 = createAttachBoard(board, attach1, 1);
//        AttachBoard attachBoard2 = createAttachBoard(board, attach2, 2);
//        AttachBoard attachBoard3 = createAttachBoard(board, attach3, 3);
//        attachBoardRepository.saveAll(List.of(attachBoard1, attachBoard2, attachBoard3));
//
//        // when
//        AttachBoardGetResponse response = attachService.getBoardImages(savedBoard.getId());
//
//        // then
//        assertThat(response.getAttachBoards()).hasSize(3)
//                .extracting("imagePath")
//                .containsExactlyInAnyOrder("test1.jpg", "test2.jpg", "test3.jpg");
//    }
//
//    @DisplayName("존재하지 않는 게시글의 이미지를 조회할 경우, 예외가 발생한다.")
//    @Test
//    void getBoardImagesWhenBoardIsEmpty() {
//        // when & then
//        assertThatThrownBy(() -> attachService.getBoardImages(1L))
//                .isInstanceOf(BoardNotFoundException.class)
//                .hasMessage("게시글을 찾을 수 없습니다.");
//    }
//
//    @DisplayName("특정 게시글에 등록된 이미지가 없다면, 예외가 발생한다.")
//    @Test
//    void getBoardImagesWhenAttachBoardIsEmpty() {
//        // given
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Board board = createBoard(member);
//        Board savedBoard = boardRepository.save(board);
//
//        // when & then
//        assertThatThrownBy(() -> attachService.getBoardImages(savedBoard.getId()))
//                .isInstanceOf(AttachNotFoundException.class)
//                .hasMessage("해당 첨부 이미지를 찾을 수 없습니다.");
//    }
//
//    @DisplayName("모든 게시글에 등록된 이미지 목록을 조회한다.")
//    @Test
//    void getAllBoardImages() {
//        // given
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Board board1 = createBoard(member);
//        Board board2 = createBoard(member);
//        Board board3 = createBoard(member);
//        boardRepository.saveAll(List.of(board1, board2, board3));
//
//        Attach attach1 = createAttach("test1.jpg");
//        Attach attach2 = createAttach("test2.jpg");
//        Attach attach3 = createAttach("test3.jpg");
//        Attach attach4 = createAttach("test4.jpg");
//        Attach attach5 = createAttach("test5.jpg");
//        Attach attach6 = createAttach("test6.jpg");
//        Attach attach7 = createAttach("test7.jpg");
//        attachRepository.saveAll(List.of(attach1, attach2, attach3, attach4, attach5, attach6, attach7));
//
//        AttachBoard attachBoard1 = createAttachBoard(board1, attach1, 1);
//        AttachBoard attachBoard2 = createAttachBoard(board1, attach2, 2);
//        AttachBoard attachBoard3 = createAttachBoard(board1, attach3, 3);
//        AttachBoard attachBoard4 = createAttachBoard(board2, attach4, 1);
//        AttachBoard attachBoard5 = createAttachBoard(board2, attach5, 2);
//        AttachBoard attachBoard6 = createAttachBoard(board3, attach6, 1);
//        AttachBoard attachBoard7 = createAttachBoard(board3, attach7, 2);
//        attachBoardRepository.saveAll(List.of(attachBoard1, attachBoard2, attachBoard3, attachBoard4, attachBoard5, attachBoard6, attachBoard7));
//
//        // when
//        AttachBoardGetAllResponse response = attachService.getAllBoardImages();
//
//        // then
//        assertThat(response.getAttachBoards()).hasSize(3)
//                .containsKeys(board1.getId(), board2.getId(), board3.getId())
//                .satisfies((attachBoard) -> {
//                    assertThat(attachBoard.get(board1.getId())).extracting("imagePath")
//                            .containsExactlyInAnyOrder("test1.jpg", "test2.jpg", "test3.jpg");
//                    assertThat(attachBoard.get(board2.getId())).extracting("imagePath")
//                            .containsExactlyInAnyOrder("test4.jpg", "test5.jpg");
//                    assertThat(attachBoard.get(board3.getId())).extracting("imagePath")
//                            .containsExactlyInAnyOrder("test6.jpg", "test7.jpg");
//                });
//    }
//
//    @DisplayName("모든 게시글에 등록된 이미지를 모두 조회할 때 이미지가 하나도 없다면, 예외가 발생한다.")
//    @Test
//    void getAllBoardImagesWhenAttachBoardIsEmpty() {
//        // given
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Board board1 = createBoard(member);
//        Board board2 = createBoard(member);
//        Board board3 = createBoard(member);
//        boardRepository.saveAll(List.of(board1, board2, board3));
//
//        // when & then
//        assertThatThrownBy(() -> attachService.getAllBoardImages())
//                .isInstanceOf(AttachNotFoundException.class)
//                .hasMessage("해당 첨부 이미지를 찾을 수 없습니다.");
//    }
//
//    @DisplayName("1번 게시글에 대한 1장의 이미지 URL(S3 이미지 객체 URL)을 DB에 저장한다.")
//    @Test
//    void singleImageUpload() {
//        // given
//        List<String> images = List.of("test1.jpg", "test2.jpg", "test3.png");
//        AttachServiceBoardCreateRequest request = AttachServiceBoardCreateRequest.builder()
//                .boardId(1L)
//                .images(images)
//                .build();
//
//        // when
//        AttachBoardCreateResponse response = attachService.createBoardImages(request);
//
//        // then
//        assertThat(response.getCode()).isEqualTo(200);
//        assertThat(response.getMessage()).isEqualTo("정상적으로 DB에 이미지 업로드를 완료했습니다.");
//        assertThat(response.isData()).isTrue();
//
//    }
//
////    @DisplayName("1번 게시글에 대해 5장의 이미지 URL(S3 이미지 객체 URL)을 DB에 저장한다.")
////    @Test
////    void MultipleImageUpload() {
////        // given
////        Map<Long, String> imgList = new HashMap<>();
////        imgList.put(1L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test_1.png");
////        imgList.put(2L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test_2.png");
////        imgList.put(3L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test_3.png");
////        imgList.put(4L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test_4.png");
////        imgList.put(5L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_5.png");
////
////        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
////                .attachType(BOARD)
////                .ids(1L)
////                .imgList(imgList)
////                .build();
////
////        // when
////        AttachResponse.UploadResponse response = attachService.createBoardImages(request);
////
////        // then
////        assertThat(response.getCode()).isEqualTo(200);
////        assertThat(response.getMessage()).isEqualTo("정상적으로 DB에 이미지 업로드를 완료했습니다.");
////        assertThat(response.isData()).isTrue();
////
////    }
////
////    @DisplayName("DB에 이미지 객체 URL이 빈 이미지 업로드 요청을 할 경우 예외가 발생한다.")
////    @Test
////    void ZeroUploadImage() {
////        // given
////        Map<Long, String> imgList = new HashMap<>();
////
////        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
////                .attachType(BOARD)
////                .ids(1L)
////                .imgList(imgList)
////                .build();
////
////        // when & then
////        assertThatThrownBy(() -> attachService.createBoardImages(request))
////                .isInstanceOf(ImageUploadOutOfRangeException.class)
////                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");
////
////    }
////
////    @DisplayName("DB에는 6장의 이미지 객체 URL을 업로드할 경우 예외가 발생한다.")
////    @Test
////    void MoreThenFiveUploadImage() {
////        // given
////        Map<Long, String> imgList = new HashMap<>();
////        imgList.put(1L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_1.png");
////        imgList.put(2L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_2.png");
////        imgList.put(3L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_3.png");
////        imgList.put(4L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_4.png");
////        imgList.put(5L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_5.png");
////        imgList.put(6L, "https://saramara-storage.s3.ap-northeast-2.amazonaws.com//test_6.png");
////
////        AttachServiceRequest.UploadRequest request = AttachServiceRequest.UploadRequest.builder()
////                .attachType(BOARD)
////                .ids(1L)
////                .imgList(imgList)
////                .build();
////
////        // when & then
////        assertThatThrownBy(() -> attachService.createBoardImages(request))
////                .isInstanceOf(ImageUploadOutOfRangeException.class)
////                .hasMessage("이미지는 최소 1장 이상, 최대 5장까지 등록할 수 있습니다.");
////
////    }
////
////    @DisplayName("하나의 게시글에 등록된 3개의 이미지 중 첫번째 이미지를 새로운 이미지로 수정한다.")
////    @Test
////    void updateImage() {
////        // given
////        Attach attach1 = createAttach(1L, 1L, "first1.jpg");
////        Attach attach2 = createAttach(1L, 2L, "second2.jpg");
////        Attach attach3 = createAttach(1L, 3L, "third3.jpg");
////        attachRepository.saveAll(List.of(attach1, attach2, attach3));
////
////        AttachServiceRequest.UpdateRequest request = AttachServiceRequest.UpdateRequest.builder()
////                .attachId(attach1.getAttachId())
////                .attachType(BOARD)
////                .ids(1L)
////                .imgPath("first_1.png")
////                .build();
////
////        // when
////        AttachResponse.UpdateResponse response = attachService.updateBoardImage(request);
////
////        // then
////        assertThat(response.getCode()).isEqualTo(200);
////        assertThat(response.getMessage()).isEqualTo("정상적으로 게시글의 이미지를 수정했습니다.");
////        assertThat(response.isData()).isTrue();
////    }
////
////    @DisplayName("하나의 게시글에 등록된 하나의 이미지를 삭제한다.")
////    @Test
////    void removeImage() {
////        // given
////        Attach attach1 = createAttach(1L, 1L, "first1.jpg");
////        Attach attach2 = createAttach(1L, 2L, "second2.jpg");
////        Attach attach3 = createAttach(1L, 3L, "third3.jpg");
////        attachRepository.saveAll(List.of(attach1, attach2, attach3));
////
////        // when
////        AttachResponse.DeleteResponse response = attachService.deleteBoardImage(attach1.getAttachId());
////
////        // then
////        assertThat(response.getCode()).isEqualTo(204);
////        assertThat(response.getMessage()).isEqualTo("정상적으로 이미지를 삭제했습니다.");
////        assertThat(response.isData()).isTrue();
////
////    }
//
//    private Member createMember() {
//        return Member.builder()
//                .nickname("test")
//                .email("test@test.com")
//                .password("test1234")
//                .type(Type.LOCAL)
//                .build();
//    }
//
//    private Board createBoard(Member member) {
//        return Board.builder()
//                .member(member)
//                .title("title!")
//                .content("content ...")
//                .categoryBoard(CategoryBoard.VOTE)
//                .deadLine(LocalDateTime.now())
//                .build();
//    }
//
//    private Attach createAttach(String imagePath) {
//        return Attach.builder()
//                .imagePath(imagePath)
//                .build();
//    }
//
//    private AttachBoard createAttachBoard(Board board, Attach attach1, int imageOrder) {
//        return AttachBoard.builder()
//                .board(board)
//                .attach(attach1)
//                .imageOrder(imageOrder)
//                .build();
//    }
//
//}