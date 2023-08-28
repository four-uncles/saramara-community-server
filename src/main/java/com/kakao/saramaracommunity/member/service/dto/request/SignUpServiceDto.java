package com.kakao.saramaracommunity.member.service.dto.request;


import com.kakao.saramaracommunity.member.entity.Member;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpServiceDto {

	private String email;
	private String nickname;
	private String password;

	@Builder
	private SignUpServiceDto(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public static SignUpServiceDto from(Member userEntity){
		if(userEntity == null) return null;
		return SignUpServiceDto.builder()
			.email(userEntity.getEmail())
			.nickname(userEntity.getNickname())
			.build();
	}

}
