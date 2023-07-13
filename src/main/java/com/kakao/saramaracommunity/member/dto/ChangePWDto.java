package com.kakao.saramaracommunity.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ChangePWDto {
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
	private String currentPassword;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
	private String changedPassword;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")
	private String changedPasswordCheck;

	public ChangePWDto(String currentPassword, String changedPassword, String changedPasswordCheck){
		this.currentPassword = currentPassword;
		this.changedPassword = changedPassword;
		this.changedPasswordCheck = changedPasswordCheck;
	}
}
