package com.kakao.saramaracommunity.member.controller.request;

import lombok.Builder;

public record MemberRegisterRequest (
	String email,
	String password,
	String nickname
) {

	@Builder
	public static MemberRegisterRequest of (String email, String password, String nickname) {
		return new MemberRegisterRequest(email, password, nickname);
	}
}
