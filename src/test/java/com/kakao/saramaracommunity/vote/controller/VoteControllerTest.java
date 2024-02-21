package com.kakao.saramaracommunity.vote.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteCreateRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

class VoteControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("게시글에 업로드된 이미지 투표 시")
    class 게시글에_업로드된_이미지_투표_시 {

        @DisplayName("이미지 1장을 선택하여 정상적으로 투표할 수 있다.")
        @Test
        void 이미지_1장을_선택하여_정상적으로_투표할_수_있다() throws Exception {
            // given
            VoteCreateRequest request = new VoteCreateRequest(
                    1L, 1L, 1L
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/vote")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 투표를 완료하였습니다."));
        }

    }

    @Nested
    @DisplayName("게시글에 업로드된 이미지 투표 조회 시")
    class 게시글에_업로드된_이미지_투표_조회_시 {

        @DisplayName("투표를 진행한 회원은 투표를 조회할 수 있다.")
        @Test
        void 투표를_진행한_회원은_투표를_조회할_수_있다() throws Exception {
            // given
            Long boardId = 1L;
            Long memberId = 1L;

            // when & then
            mockMvc.perform(
                            get("/api/v1/vote/" + boardId)
                                    .header("memberId", memberId.toString())
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으 게시글의 투표 상태를 조회하였습니다."));
        }

    }

    @Nested
    @DisplayName("게시글에 업로드된 이미지 투표 수정 시")
    class 게시글에_업로드된_이미지_투표_수정_시 {

        private static final Long voteId = 1L;

        @DisplayName("올바른 정보는 정상적으로 수정할 수 있다.")
        @Test
        void 올바른_정보는_정상적으로_수정할_수_있다() throws Exception {
            // given
            VoteUpdateRequest request = new VoteUpdateRequest(
                    1L,
                    BoardImage.builder()
                            .path("image-2")
                            .build()
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/vote/" + voteId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공적으로 투표를 수정 하였습니다."));
        }

    }

}