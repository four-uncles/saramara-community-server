package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.dto.api.request.MemberLoginRequest;
import com.kakao.saramaracommunity.member.dto.business.request.MemberCreateServiceRequest;
import com.kakao.saramaracommunity.member.dto.business.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.entity.Member;

public interface MemberService {

	void createMember(MemberCreateServiceRequest request);

	MemberInfoResponse getMemberInfo(String email);

	Member localLogin(MemberLoginRequest request);
}
