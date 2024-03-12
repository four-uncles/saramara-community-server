package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.dto.business.request.MemberCreateServiceRequest;
import com.kakao.saramaracommunity.member.dto.business.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.kakao.saramaracommunity.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceImplTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	@AfterEach
	void tearDown() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("회원가입을 통해 새로운 회원을 등록한다.")
	void 회원가입을_통해_새로운_회원을_등록한다() {
	    // given
		MemberCreateServiceRequest request = MemberCreateServiceRequest.of(
				NORMAL_MEMBER_LANGO.getEmail(),
				NORMAL_MEMBER_LANGO.getPassword(),
				NORMAL_MEMBER_LANGO.getNickname()
		);

	    // when
		memberService.createMember(request);

	    // then
		Optional<Member> result = memberRepository.findByEmail(NORMAL_MEMBER_LANGO.getEmail());
		assertThat(result.get().getEmail()).isEqualTo(NORMAL_MEMBER_LANGO.getEmail());
		assertThat(result.get().getNickname()).isEqualTo(NORMAL_MEMBER_LANGO.getNickname());
	}

	@Test
	@DisplayName("가입한 회원이라면 프로필 정보를 조회할 수 있다.")
	void 회원_프로필_정보를_조회할_수_있다() {
	    // given
		Member member = NORMAL_MEMBER_LANGO.createMember();
		memberRepository.save(member);

		// when
		MemberInfoResponse result = memberService.getMemberInfo(member.getEmail());

		// then
		assertThat(result.email()).isEqualTo(member.getEmail());
		assertThat(result.nickname()).isEqualTo(member.getNickname());
	}

}
