package com.kakao.saramaracommunity.member.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticationDetail extends User {

	private String username;

	public AuthenticationDetail(String username, String password) {
		this(
			username,
			password,
			Collections.singletonList(
				new SimpleGrantedAuthority("ROLE_MEMBER")
			)
		);
		this.username = username;
	}

	public AuthenticationDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	@Override
	public String getUsername() {
		return this.username;
	}
}
