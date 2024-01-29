package com.kakao.saramaracommunity.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.controller.request.MemberLoginRequest;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService, UserDetailsService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void registerMember(MemberRegisterRequest request) {
		// Member newMember = Member.of(request);
		Member build = Member.builder()
			.email(request.email())
			.password(passwordEncoder.encode(request.password()))
			.nickname(request.nickname())
			.build();
		memberRepository.save(build);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfoByEmail(String email) {
		Member memberInfo = memberRepository.findMemberByEmail(email).orElseThrow(()-> new RuntimeException("NOT FOUND MEMBER"));
		return MemberInfoResponse.from(memberInfo);
	}

	@Override
	public Member login(MemberLoginRequest request) {
		Member member = memberRepository.findMemberByEmail(request.email()).orElseThrow(()-> new RuntimeException("NOT FOUND MEMBER"));

		if (!member.getPassword().equals(request.password())) {
			throw new RuntimeException("비밀번호가 틀렸습니다.");
		}

		return member;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Member member = memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new RuntimeException("NOT FOUND MEMBER"));

		return new AuthenticationDetail(email, member.getPassword());
	}
}
