package com.kakao.saramaracommunity.comment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kakao.saramaracommunity.comment.dto.api.request.CommentCreateRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentDeleteRequest;
import com.kakao.saramaracommunity.comment.dto.api.request.CommentUpdateRequest;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

class CommentControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("댓글 작성 시")
    class 댓글_작성_시 {

        @DisplayName("올바른 정보는 정상적으로 등록할 수 있다.")
        @Test
        void 올바른_정보는_정상적으로_등록할_수_있다() throws Exception {
            // given
            CommentCreateRequest request = new CommentCreateRequest(
                    1L, 1L, "1번 제품이 합리적이네요!"
            );

            // when & then
            mockMvc.perform(
                    post("/api/v1/comment/register")
                            .content(objectMapper.writeValueAsString(request))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공적으로 댓글을 작성 하였습니다."));
        }

        @DisplayName("작성자의 정보는 반드시 입력되어야 한다.")
        @Test
        void 작성자의_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            CommentCreateRequest request = new CommentCreateRequest(
                    null, 1L, "1번 제품이 합리적이네요!"
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/comment/register")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[회원 정보는 필수 입니다.]"));
        }

        @DisplayName("게시글의 정보는 반드시 입력되어야 한다")
        @Test
        void 게시글의_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            CommentCreateRequest request = new CommentCreateRequest(
                    1L, null, "1번 제품이 합리적이네요!"
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/comment/register")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[게시글 정보는 필수 입니다.]"));
        }

        @DisplayName("내용은 반드시 입력되어야 한다.")
        @Test
        void 내용은_반드시_입력되어야_한다() throws Exception {
            // given
            CommentCreateRequest request = new CommentCreateRequest(
                    1L, 1L, null
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/comment/register")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[댓글 내용은 필수 입니다.]"));
        }

        @DisplayName("내용은 공란일 수 없다")
        @Test
        void 내용은_공란일_수_없다() throws Exception {
            // given
            CommentCreateRequest request = new CommentCreateRequest(
                    1L, 1L, " "
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/comment/register")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[댓글 내용은 필수 입니다.]"));
        }

    }

    @Nested
    @DisplayName("댓글 조회 시")
    class 댓글_조회_시 {

        @DisplayName("하나의 게시글에 등록된 내용을 모두 조회할 수 있다.")
        @Test
        void 하나의_게시글에_등록된_내용을_모두_조회할_수_있다() throws Exception {
            // given
            Long boardId = 1L;

            // when & then
            mockMvc.perform(
                    get("/api/v1/comment/" + boardId + "/comments")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 게시글의 댓글을 모두 조회 하였습니다."));

        }

    }

    @Nested
    @DisplayName("댓글 수정 시")
    class 댓글_수정_시 {

        private static final Long commentId = 1L;

        @DisplayName("올바른 정보는 정상적으로 수정할 수 있다.")
        @Test
        void 올바른_정보는_정상적으로_수정할_수_있다() throws Exception {
            // given
            CommentUpdateRequest request = new CommentUpdateRequest(
                    1L, "2번 제품이 합리적이네요!"
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/comment/" + commentId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공적으로 댓글을 수정 하였습니다."));

        }

        @DisplayName("작성자의 정보는 반드시 입력되어야 한다.")
        @Test
        void 작성자의_정보는_반드시_입력되어야_한다() throws Exception {
            // given
            CommentUpdateRequest request = new CommentUpdateRequest(
                    null, "1번 제품이 합리적이네요!"
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/comment/" + commentId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[회원 정보는 필수 입니다.]"));
        }

        @DisplayName("내용은 반드시 입력되어야 한다.")
        @Test
        void 내용은_반드시_입력되어야_한다() throws Exception {
            // given
            CommentUpdateRequest request = new CommentUpdateRequest(
                    1L, null
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/comment/" + commentId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[댓글 내용은 필수 입니다.]"));
        }

        @DisplayName("내용은 공란일 수 없다")
        @Test
        void 내용은_공란일_수_없다() throws Exception {
            // given
            CommentUpdateRequest request = new CommentUpdateRequest(
                    1L, " "
            );

            // when & then
            mockMvc.perform(
                            patch("/api/v1/comment/" + commentId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.message").value("[댓글 내용은 필수 입니다.]"));
        }
    }

    @Nested
    @DisplayName("댓글 삭제 시")
    class 댓글_삭제_시 {

        private static final Long commentId = 1L;

        @DisplayName("올바른 정보는 정상적으로 삭제할 수 있다.")
        @Test
        void 올바른_정보는_정상적으로_삭제할_수_있다() throws Exception {
            // given
            CommentDeleteRequest request = new CommentDeleteRequest(1L);

            // when & then
            mockMvc.perform(
                            delete("/api/v1/comment/" + commentId)
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공적으로 댓글을 삭제 하였습니다."));

        }

        @DisplayName("작성자의 정보는 반드시 입력되어야 한다.")
        @Test
        void 작성자의_정보는_반드시_필요하다() throws Exception {
            // given
            CommentDeleteRequest request = new CommentDeleteRequest(null);

            // when & then
            mockMvc.perform(
                            delete("/api/v1/comment/" + commentId)
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