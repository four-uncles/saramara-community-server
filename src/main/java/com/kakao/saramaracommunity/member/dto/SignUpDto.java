package com.kakao.saramaracommunity.member.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.saramaracommunity.member.entity.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
	@NotNull(message = "이메일은 필수 입력 값입니다.")
	@Size(max = 100, message = "이메일은 최대 100자까지 입력 가능합니다.")
	@Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Setter
	private String email;

	@NotNull(message = "닉네임은 필수 입력 값입니다.")
	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9-_]{2,10}$")
	@Setter
	private String nickname;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
	@Setter
	private String password;



	public static SignUpDto from(Member userEntity){
		if(userEntity == null) return null;

		return SignUpDto.builder()
			.email(userEntity.getEmail())
			.nickname(userEntity.getNickname())
			.build();
	}

}
