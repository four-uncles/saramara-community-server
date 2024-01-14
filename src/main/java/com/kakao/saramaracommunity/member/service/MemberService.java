package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;

public interface MemberService {

	void registerMember(MemberRegisterRequest request);

	MemberInfoResponse getMemberInfoByEmail(String email);
}
