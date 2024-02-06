package com.kakao.saramaracommunity.member.controller.request;

import org.hibernate.annotations.Target;

import jakarta.validation.constraints.NotNull;

public record MemberLoginRequest (
	String email,
	String password
) {
	public static MemberLoginRequest of (String email, String password) {
		return new MemberLoginRequest (email, password);
	}
}
