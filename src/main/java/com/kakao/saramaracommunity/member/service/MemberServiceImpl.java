package com.kakao.saramaracommunity.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
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
		Member newMember = Member.register(request);

		memberRepository.save(newMember);
	}
}
