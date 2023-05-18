package com.kakao.saramaracommunity.member.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SecurityMemberDto {

	@NotNull
	private Long memberId;

	@NotNull
	@Size(max = 50)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(min = 3, max = 100)
	private String password;

	@Size(min = 3, max = 50)
	private String nickname;

	private String picture;

	private Type type;

	private Set<Role> role;

	public static SecurityMemberDto from(Member userEntity){
		if(userEntity == null) return null;

		return SecurityMemberDto.builder()
			.memberId(userEntity.getMemberId())
			.email(userEntity.getEmail())
			.nickname(userEntity.getNickname())
			.role(userEntity.getRole().stream()
				.map(role1 -> Role.USER)
				.collect(Collectors.toSet()))
			.picture(userEntity.getPicture())
			.type(userEntity.getType())
			.build();
	}

}
