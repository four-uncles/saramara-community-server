package com.kakao.saramaracommunity.member.dto.api.request;

import com.kakao.saramaracommunity.member.dto.business.request.MemberCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(
	@Pattern(regexp = "^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$", message = "이메일 형식이 올바르지 않습니다.")
	String email,
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,16}$", message = "비밀번호는 최소 8자 이상, 최대 16자 이하로 숫자와 특수문자를 반드시 포함해야 합니다.")
	String password,
	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	@Size(min = 1, max = 10, message = "닉네임은 최소 1자 이상, 최대 10자 이하까지 등록할 수 있습니다.")
	String nickname
) {
	public MemberCreateServiceRequest toServiceRequest() {
		return MemberCreateServiceRequest.of(
				email,
				password,
				nickname
		);
	}

	public static MemberCreateRequest of(
			String email,
			String password,
			String nickname
	) {
		return new MemberCreateRequest(email, password, nickname);
	}
}
