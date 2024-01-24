package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.controller.request.MemberLoginRequest;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;

public interface MemberService {

	void registerMember(MemberRegisterRequest request);

	MemberInfoResponse getMemberInfoByEmail(String email);

	Member login(MemberLoginRequest request);
}
