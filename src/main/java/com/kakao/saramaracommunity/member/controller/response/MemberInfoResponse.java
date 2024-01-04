package com.kakao.saramaracommunity.member.controller.response;

import com.kakao.saramaracommunity.member.entity.Member;

public record MemberInfoResponse(
	String email,
	String password,
	String nickname
) {
	public static MemberInfoResponse from (Member member) {
		return new MemberInfoResponse(
			member.getEmail(),
			member.getPassword(),
			member.getNickname()
		);
	}
}
