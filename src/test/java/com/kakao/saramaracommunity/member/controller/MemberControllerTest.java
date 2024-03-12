package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.member.dto.api.request.MemberCreateRequest;
import com.kakao.saramaracommunity.support.ControllerTestSupport;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 회원_가입_시 {
        @Test
        @DisplayName("신규 회원으로 가입할 수 있다.")
        void 신규_회원으로_가입할_수_있다() throws Exception {
            // given
            MemberCreateRequest request = MemberCreateRequest.of(
                    NORMAL_MEMBER_LANGO.getEmail(),
                    NORMAL_MEMBER_LANGO.getPassword(),
                    NORMAL_MEMBER_LANGO.getNickname()
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/member")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 회원가입을 완료하였습니다."));
        }

        @ParameterizedTest
        @DisplayName("이메일 형식이 올바르지 않다면 400 예외를 반환한다.")
        @ValueSource(strings = {"lango@", "@lango", "lango@1234", "@lango.com"})
        void 이메일_형식이_올바르지_않다면_400_예외를_반환한다(String email) throws Exception {
            // given
            MemberCreateRequest request = MemberCreateRequest.of(
                    email,
                    NORMAL_MEMBER_LANGO.getPassword(),
                    NORMAL_MEMBER_LANGO.getNickname()
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/member")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[이메일 형식이 올바르지 않습니다.]"));
        }
        @ParameterizedTest
        @DisplayName("비밀번호 형식이 올바르지 않다면 400 예외를 반환한다.")
        @ValueSource(strings = {"saramara", "123123!!!", "Saramara123", "Sa123!"})
        void 비밀번호_형식이_올바르지_않다면_400_예외를_반환한다(String password) throws Exception {
            // given
            MemberCreateRequest request = MemberCreateRequest.of(
                    NORMAL_MEMBER_LANGO.getEmail(),
                    password,
                    NORMAL_MEMBER_LANGO.getNickname()
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/member")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[비밀번호는 최소 8자 이상, 최대 16자 이하로 숫자와 특수문자를 반드시 포함해야 합니다.]"));
        }
        @Test
        @DisplayName("등록할 닉네임의 길이가 10자를 초과한다면 400 예외를 반환한다.")
        void 등록할_닉네임의_길이가_10자를_초과한다면_400_예외를_반환한다() throws Exception {
            // given
            MemberCreateRequest request = MemberCreateRequest.of(
                    NORMAL_MEMBER_LANGO.getEmail(),
                    NORMAL_MEMBER_LANGO.getPassword(),
                    "12345678901"
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/member")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[닉네임은 최소 1자 이상, 최대 10자 이하까지 등록할 수 있습니다.]"));
        }
        @ParameterizedTest
        @DisplayName("등록할 닉네임이 공백 문자열로 입력되었다면 400 예외를 반환한다.")
        @ValueSource(strings = {" ", "  ", "       "})
        void 등록할_닉네임이_공백_문자열로_입력되었다면_400_예외를_반환한다(String nickname) throws Exception {
            // given
            MemberCreateRequest request = MemberCreateRequest.of(
                    NORMAL_MEMBER_LANGO.getEmail(),
                    NORMAL_MEMBER_LANGO.getPassword(),
                    nickname
            );

            // when & then
            mockMvc.perform(
                            post("/api/v1/member")
                                    .content(objectMapper.writeValueAsString(request))
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("[닉네임은 공백일 수 없습니다.]"));
        }
    }

    @Nested
    @DisplayNameGeneration(ReplaceUnderscores.class)
    class 회원_프로필_정보_조회_시 {
        @Test
        @DisplayName("가입한 회원의 정보를 조회할 수 있다.")
        void 가입한_회원의_정보를_조회할_수_있다() throws Exception {
            // given
            String email = NORMAL_MEMBER_LANGO.getEmail();

            // when & then
            mockMvc.perform(
                            get("/api/v1/member/" + email)
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공적으로 회원의 프로필 정보를 조회하였습니다."));
        }

        /**
         * 현재 회원 컨트롤러에서는 회원 프로필 정보 조회에 대한 유효성 검사를 진행하고 있지 않습니다.
         * 추후, 유효성 검사 추가후 테스트할 예정입니다.
         */
        @Disabled
        @Test
        @DisplayName("회원의 이메일 정보가 없거나 유효하지 않은 이메일이라면 400 예외를 반환한다.")
        void 회원의_이메일_정보가_없거나_유효하지_않은_이메일이라면_400_예외를_반환한다() throws Exception {
            // when & then
            mockMvc.perform(
                            get("/api/v1/member/" + "xxx@")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.message").value("성공적으로 회원의 프로필 정보를 조회하였습니다."));
        }
    }

}
