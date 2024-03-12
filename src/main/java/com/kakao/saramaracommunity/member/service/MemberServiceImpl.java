package com.kakao.saramaracommunity.member.service;

import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.*;

import com.kakao.saramaracommunity.member.dto.business.request.MemberCreateServiceRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.dto.api.request.MemberLoginRequest;
import com.kakao.saramaracommunity.member.dto.business.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void createMember(MemberCreateServiceRequest request) {
		Member member = Member.builder()
			.email(request.email())
			.password(passwordEncoder.encode(request.password()))
			.nickname(request.nickname())
			.build();
		memberRepository.save(member);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo(String email) {
		Member memberInfo = memberRepository.findByEmail(email)
				.orElseThrow(()-> new MemberBusinessException(MEMBER_NOT_FOUND));
		return MemberInfoResponse.from(memberInfo);
	}

	@Override
	public Member localLogin(MemberLoginRequest request) {
		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(()-> new MemberBusinessException(MEMBER_NOT_FOUND));
		if (!member.getPassword().equals(request.password())) {
			throw new MemberBusinessException(WRONG_PASSWORD);
		}
		return member;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberBusinessException(MEMBER_NOT_FOUND));
		return new AuthenticationDetail(email, member.getPassword());
	}
}
