package com.kakao.saramaracommunity.member.entity;

import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.kakao.saramaracommunity.fixture.MemberFixture.NORMAL_MEMBER_LANGO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberTest {

    @Test
    @DisplayName("회원 객체를 정상적으로 생성할 수 있다.")
    void 회원_객체를_생성할_수_있다() {
        // given & when & then
        assertDoesNotThrow(() -> new Member(
                NORMAL_MEMBER_LANGO.getEmail(),
                NORMAL_MEMBER_LANGO.getPassword(),
                NORMAL_MEMBER_LANGO.getNickname()
        ));
    }

    @Disabled
    @ParameterizedTest
    @DisplayName("회원의 이메일 형식이 올바르지 않다면 예외가 발생한다.")
    @ValueSource(strings = {"lango.test@", "lango.test@amail", "@lango.saramara"})
    void 회원의_이메일_형식이_올바르지_않다면_예외가_발생한다(final String email) {
        // given & when & then
        assertThatThrownBy(() -> new Member(
                email,
                NORMAL_MEMBER_LANGO.getPassword(),
                NORMAL_MEMBER_LANGO.getNickname()
        )).isInstanceOf(MemberBusinessException.class);
    }

    @Disabled
    @DisplayName("회원의 비밀번호 형식이 올바르지 않다면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"saramara", "saramara!", "Sa123!", "Saramara!", "Saramara123"})
    void 회원의_비밀번호_형식이_올바르지_않다면_예외가_발생한다(final String password) {
        // given & when & then
        assertThatThrownBy(() -> new Member(
                NORMAL_MEMBER_LANGO.getEmail(),
                password,
                NORMAL_MEMBER_LANGO.getNickname()
        )).isInstanceOf(MemberBusinessException.class);
    }

    @Disabled
    @DisplayName("회원의 닉네임이 공백이거나 비어있다면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 회원의_닉네임이_공백이거나_비어있다면_예외가_발생한다(final String nickname) {
        // given & when & then
        assertThatThrownBy(() -> new Member(
                NORMAL_MEMBER_LANGO.getEmail(),
                NORMAL_MEMBER_LANGO.getPassword(),
                nickname
        )).isInstanceOf(MemberBusinessException.class);
    }

    @Disabled
    @DisplayName("회원의 닉네임이 10자를 초과할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"saramaraaaa", "saramaraaaaaaaaaaaa"})
    void 회원의_닉네임이_10자를_초과할_경우_예외가_발생한다(final String nickname) {
        // given & when & then
        assertThatThrownBy(() -> new Member(
                NORMAL_MEMBER_LANGO.getEmail(),
                NORMAL_MEMBER_LANGO.getPassword(),
                nickname
        )).isInstanceOf(MemberBusinessException.class);
    }

}
