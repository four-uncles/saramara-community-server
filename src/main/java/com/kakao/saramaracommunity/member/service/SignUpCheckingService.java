package com.kakao.saramaracommunity.member.service;

import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.MemberResDto;

@Service
public interface SignUpCheckingService {

	// 이메일 중복확인 기능에 대한 동작
	MemberResDto checkingDuplicateEmail(String email);

	// 닉네임 중복확인 기능에 대한 동작
	MemberResDto checkingDuplicatedNickName(String nickname);

	// 이메일 인증
	MemberResDto authenticatingEmail(String email);

}
