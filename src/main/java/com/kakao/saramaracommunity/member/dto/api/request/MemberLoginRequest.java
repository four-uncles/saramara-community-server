package com.kakao.saramaracommunity.member.dto.api.request;

public record MemberLoginRequest (
	String email,
	String password
) {
	public static MemberLoginRequest of(
			String email,
			String password
	) {
		return new MemberLoginRequest(email, password);
	}
}
