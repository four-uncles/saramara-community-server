package com.kakao.saramaracommunity.member.dto;

import java.util.Set;

import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;

import lombok.Builder;
import lombok.Setter;

@Setter
public class MemberInfoResDto {
	private String email;
	private String nickname;
	private Type type;
	private Set<Role> role;

	@Builder
	public MemberInfoResDto(String email, String nickname, Type type, Set<Role> role){
		this.email = email;
		this.nickname = nickname;
		this.type = type;
		this.role = role;
	}
}
