package com.kakao.saramaracommunity.member.service;

import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SignUpCheckingServiceImpl implements SignUpCheckingService{
	private final MemberRepository memberRepository;
	private final MemberServiceMethod memberServiceMethod;

	// 사용자가 '이메일 중복 확인' 를 선택했을 때 처리
	@Override
	public MemberResDto checkingDuplicateEmail(String email) {
		boolean duplicatedEmail = memberServiceMethod.isDuplicatedEmail(
			memberRepository.countByEmail(email)
		);

		// 중복된 이메일에 대한 예외처리
		if (duplicatedEmail) {
			MemberResDto response = memberServiceMethod.makeDuplicateEmailResult();
			return response;
		}

		// 사용자가 사용하고자 하는 이메일이 중복되지 않는 경우
		MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
		return response;
	}

	// 사용자가 '닉네임 중복 확인' 를 선택했을 때 처리
	@Override
	public MemberResDto checkingDuplicatedNickName(String nickname) {
		boolean duplicatedNickname = memberServiceMethod.isDuplicatedNickname(
			memberRepository.countByNickname(nickname)
		);

		// 중복된 닉네임에 대한 예외처리
		if(duplicatedNickname){
			MemberResDto response = memberServiceMethod.makeDuplicatedNicknameResult();
			return response;
		}

		// 사용자가 사용하고자 하는 닉네임이 중복되지 않는 경우
		MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
		return response;
	}

	@Override
	public MemberResDto authenticatingEmail(String email) {
		return null;
	}
}
