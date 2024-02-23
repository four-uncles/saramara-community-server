package com.kakao.saramaracommunity.board.controller;

import com.kakao.saramaracommunity.board.dto.api.reqeust.BoardCreateRequest;
import com.kakao.saramaracommunity.board.dto.api.reqeust.BoardDeleteRequest;
import com.kakao.saramaracommunity.board.dto.api.reqeust.BoardUpdateRequest;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.time.LocalDateTime;
import java.util.List;

import static com.kakao.saramaracommunity.board.entity.CategoryBoard.VOTE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 게시글(Board) 관련 프레젠테이션 계층의 슬라이스 테스트를 진행하는 테스트 클래스입니다.
 * 공통 응답으로 사용하는 ApiResponse의 @JsonInclude 설정으로 인해 data가 null일 경우라면 검증할 필요가 없습니다.
 */
class BoardControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("새로운 게시글 생성 시")
    class 새로운_게시글_생성_시 {
        @Test
        @DisplayName("정상적으로 등록할 수 있다.")
        void 정상적으로_등록할_수_있다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 게시글 작성을 완료하였습니다."));
        }
        @Test
        @DisplayName("제목은 공란일 수 없다.")
        void 제목은_공란일_수_없다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 제목을 입력해주세요.]"));
        }
        @Test
        @DisplayName("내용은 공란일 수 없다.")
        void 내용은_공란일_수_없다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 내용을 입력해주세요.]"));
        }
        @Test
        @DisplayName("카테고리는 반드시 입력되어야 한다.")
        void 카테고리는_반드시_입력되어야_한다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 카테고리가 선택되지 않았습니다.]"));
        }
        @Test
        @DisplayName("작성자 정보는 반드시 입력되어야 한다.")
        void 작성자_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[존재하지 않는 사용자입니다.]"));
        }
        @Test
        @DisplayName("이미지 목록은 1장 이상 등록되어야 한다.")
        void 이미지_목록은_1장_이상_등록되어야_한다() throws Exception {
            // given
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .build();

            // when & then
            mockMvc.perform(
                            post("/api/v1/board")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[이미지는 최소 1장 이상 등록해야 합니다.]"));
        }
    }
    
    @Nested
    @DisplayName("게시글 수정 시")
    class 게시글_수정_시 {
        @Test
        @DisplayName("정상적으로 변경할 수 있다.")
        void 정상적으로_변경할_수_있다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요? 이미지를 새로 등록했으니 다시 투표해주세요.")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03", "image-04"))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 게시글을 수정하였습니다."))
                    .andExpect(jsonPath("$.data").isBoolean());
        }
        @Test
        @DisplayName("제목은 공란일 수 없다.")
        void 제목은_공란일_수_없다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 제목을 입력해주세요.]"));
        }
        @Test
        @DisplayName("내용은 공란일 수 없다.")
        void 내용은_공란일_수_없다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 내용을 입력해주세요.]"));
        }
        @Test
        @DisplayName("카테고리는 반드시 입력되어야 한다.")
        void 카테고리는_반드시_입력되어야_한다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글의 카테고리가 선택되지 않았습니다.]"));
        }
        @Test
        @DisplayName("작성자 정보는 반드시 입력되어야 한다.")
        void 작성자_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .boardImages(List.of("image-01", "image-02", "image-03"))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[존재하지 않는 사용자입니다.]"));
        }
        @Test
        @DisplayName("이미지 목록은 1장 이상 등록되어야 한다.")
        void 이미지_목록은_1장_이상_등록되어야_한다() throws Exception {
            // given
            Long boardId = 1L;
            BoardUpdateRequest request = BoardUpdateRequest.builder()
                    .title("캠핑 제품을 구매하려는데 골라주세요.")
                    .content("위 3가지 제품 중 무엇이 가장 합리적일까요?")
                    .categoryBoard(VOTE)
                    .memberId(1L)
                    .deadLine(LocalDateTime.of(2024, 12, 31, 0, 0, 0, 0))
                    .build();

            // when & then
            mockMvc.perform(
                            patch("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[이미지는 최소 1장 이상 등록해야 합니다.]"));
        }
    }

    @Test
    @DisplayName("하나의 게시글을 조회할 수 있다.")
    void 하나의_게시글을_조회할_수_있다() throws Exception {
        // given
        Long boardId = 1L;

        // when & then
        mockMvc.perform(
                        get("/api/v1/board/" + boardId)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공적으로 게시글 정보를 조회하였습니다."));
    }

    @Test
    @DisplayName("게시글 목록을 조회할 수 있다.")
    void 게시글_목록을_조회할_수_있다() throws Exception {
        // given
        String cursorId = "1";
        String size = "24";
        String sort = "LATEST";

        // when & then
        mockMvc.perform(
                        get("/api/v1/board")
                                .param("cursorId", cursorId)
                                .param("size", size)
                                .param("sort", sort)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공적으로 모든 게시글 정보를 조회하였습니다."));
    }

    @Nested
    @DisplayName("게시글 삭제 시")
    class 게시글_삭제_시 {
        @Test
        @DisplayName("정상적으로 삭제할 수 있다.")
        void 정상적으로_삭제할_수_있다() throws Exception {
            // given
            Long boardId = 1L;
            BoardDeleteRequest request = BoardDeleteRequest.builder()
                    .memberId(1L)
                    .build();

            // when & then
            mockMvc.perform(
                            delete("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("204"))
                    .andExpect(jsonPath("$.message").value("성공적으로 게시글을 삭제하였습니다."))
                    .andExpect(jsonPath("$.data").isBoolean());

        }
        @Test
        @DisplayName("작성자 정보는 반드시 입력되어야 한다.")
        void 작성자_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            Long boardId = 1L;
            BoardDeleteRequest request = BoardDeleteRequest.builder()
                    .build();

            // when & then
            mockMvc.perform(
                            delete("/api/v1/board/" + boardId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[존재하지 않는 사용자입니다.]"));

        }
    }

}
