package com.kakao.saramaracommunity.member.service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangePWServiceDto {

	private String currentPassword;
	private String changedPassword;
	private String changedPasswordCheck;

	@Builder
	private ChangePWServiceDto(String currentPassword, String changedPassword, String changedPasswordCheck){
		this.currentPassword = currentPassword;
		this.changedPassword = changedPassword;
		this.changedPasswordCheck = changedPasswordCheck;
	}
}
