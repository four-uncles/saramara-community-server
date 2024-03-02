package com.kakao.saramaracommunity.vote.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteCreateRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteDeleteRequest;
import com.kakao.saramaracommunity.vote.dto.api.request.VoteUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

class VoteControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 신규_투표_시 {

        @Test
        @DisplayName("정상적으로 투표할 수 있다.")
        void 정상적으로_투표할_수_있다() throws Exception {
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

        @Test
        @DisplayName("투표자 정보는 필수로 입력되어야 한다.")
        void 투표자_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteCreateRequest request = new VoteCreateRequest(
                    null, 1L, 1L
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/vote")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[회원 정보는 필수 입니다.]"));
        }

        @Test
        @DisplayName("투표할 게시글의 정보는 필수로 입력되어야 한다.")
        void 투표할_게시글의_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteCreateRequest request = new VoteCreateRequest(
                    1L, null, 1L
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/vote")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[게시글 정보는 필수 입니다.]"));
        }

        @Test
        @DisplayName("투표할 항목에 대한 정보는 필수로 입력되어야 한다.")
        void 투표할_항목에_대한_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteCreateRequest request = new VoteCreateRequest(
                    1L, 1L, null
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/vote")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[투표할 이미지 정보는 필수 입니다.]"));
        }

    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 투표_여부_조회_시 {

        @Test
        @DisplayName("특정 게시글에 대한 투표 현황을 알 수 있다.")
        void 특정_게시글에_대한_투표_현황을_알_수_있다() throws Exception {
            // given
            Long boardId = 1L;

            // when & then
            mockMvc.perform(
                            get("/api/v1/vote/" + boardId)
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 게시글의 투표 상태를 조회하였습니다."));
        }

    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 재투표_시 {

        private static final Long voteId = 1L;

        @Test
        @DisplayName("정상적으로 다시 투표할 수 있다.")
        void 정상적으로_다시_투표할_수_있다() throws Exception {
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

        @Test
        @DisplayName("투표자 정보는 필수로 입력되어야 한다.")
        void 투표자_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteUpdateRequest request = new VoteUpdateRequest(
                    null,
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
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[회원 정보는 필수 입니다.]"));
        }

        @Test
        @DisplayName("투표할 항목에 대한 정보는 필수로 입력되어야 한다.")
        void 투표할_항목에_대한_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteUpdateRequest request = new VoteUpdateRequest(
                    1L,
                    null
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/vote/" + voteId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[투표할 이미지 정보는 필수 입니다.]"));
        }

    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 투표_취소_시 {

        private static final Long voteId = 1L;

        @Test
        @DisplayName("정상적으로 취소할 수 있다.")
        void 정상적으로_취소할_수_있다() throws Exception {
            // given
            VoteDeleteRequest request = new VoteDeleteRequest(1L);

            // when & then
            mockMvc.perform(
                            delete("/api/v1/vote/" + voteId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공적으로 투표를 삭제 하였습니다."));
        }

        @Test
        @DisplayName("투표자 정보는 필수로 입력되어야 한다.")
        void 투표자_정보는_필수로_입력되어야_한다() throws Exception {
            // given
            VoteDeleteRequest request = new VoteDeleteRequest(null);

            // when & then
            mockMvc.perform(
                            delete("/api/v1/vote/" + voteId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[회원 정보는 필수 입니다.]"));
        }

    }

}