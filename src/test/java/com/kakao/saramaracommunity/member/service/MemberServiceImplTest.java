package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@Disabled
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
	@InjectMocks
	private MemberServiceImpl memberService;

	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("한 명의 멤버를 가입시킨다.")
	void registerMember() {
		// given
		MemberRegisterRequest newMemberInfo = new MemberRegisterRequest("test@gmail.com", "testPwd", "testNickname");
		Member newMember = Member.of(newMemberInfo);
		given(memberRepository.existsMemberByEmail(anyString())).willReturn(true);
		given(memberRepository.findMemberByEmail(anyString())).willReturn(Optional.ofNullable(newMember));
		given(memberRepository.save(any())).willReturn(newMember);

		// when
		memberService.registerMember(newMemberInfo);
		MemberInfoResponse registeredMember = memberService.getMemberInfoByEmail(newMemberInfo.email());

		// then
		assertThat(registeredMember.email()).isEqualTo("test@gmail.com");
		assertThat(registeredMember.password()).isEqualTo("testPwd");
		assertThat(registeredMember.nickname()).isEqualTo("testNickname");
	}
}
