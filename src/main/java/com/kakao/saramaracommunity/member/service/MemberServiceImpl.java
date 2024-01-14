package com.kakao.saramaracommunity.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if (!memberRepository.existsMemberByEmail(email)) {
			// TODO: CUSTOM UNCHECKED EXCEPTION
			throw new RuntimeException("NOT FOUND MEMBER");
		}
		Member memberInfo = memberRepository.findMemberByEmail(email);
		return MemberInfoResponse.from(memberInfo);
	}
}
