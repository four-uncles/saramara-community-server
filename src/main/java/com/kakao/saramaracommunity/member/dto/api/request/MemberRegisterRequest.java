package com.kakao.saramaracommunity.member.dto.api.request;

public record MemberRegisterRequest (
	String email,
	String password,
	String nickname
) {
	public static MemberRegisterRequest of(
			String email,
			String password,
			String nickname
	) {
		return new MemberRegisterRequest(email, password, nickname);
	}
}
