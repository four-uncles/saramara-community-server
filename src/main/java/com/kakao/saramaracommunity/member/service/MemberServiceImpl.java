package com.kakao.saramaracommunity.member.service;

import java.util.Optional;

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
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public void registerMember(MemberRegisterRequest request) {
		Member newMember = Member.of(request);

		memberRepository.save(newMember);
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
}
