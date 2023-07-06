package com.kakao.saramaracommunity.member.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우(Client 의 잘못된 요청)."),
	INTERNAL_SERVER_ERROR(500, "COMMON-002", "서버에서 처리할 수 없는 경우."),

	DUPLICATE_EMAIL(409, "Member-001", "이미 존재하는 Email 입니다."),
	DUPLICATE_NICKNAME(409, "Member-002", "이미 존재하는 NickName 입니다."),
	INVALID_INPUT_PW(400, "Member-003", "비밀번호에 영문, 숫자, 특수문자가 모두 포함시켜야 하고 8~16자리를 입력해야 합니다, 공백은 입력이 불가능 합니다."),
	INVALID_INPUT_NICKNAME(400, "Member-004", "닉네임이 특수문자나 공백을 포함할 수 없으며, 2~10자리 까지 입력할 수 있습니다."),
	INVALID_INPUT_EMAIL(400, "Member-005", "올바른 이메일 형식이 아닙니다."),
	NOT_EQUALS_INPUT_CHANGED_PW(400, "Member-006", "변경할 비밀번호 입력과 변경된 비밀번호 입력이 일치하지 않습니다."),
	NOT_EQUALS_INPUT_CURRENT_PW(400, "Member-007", "현재 입력한 비밀번호가 실제 현재 비밀번호와 일치하지 않습니다."),
	NOT_EXIST_EMAIL(400, "Member-008", "Email이 존재하지 않습니다."),
	NOT_EXIST_PW(400, "Member-009", "Email은 올바르지만 비밀번호가 잘못되었습니다.");


	private int status;
	private String code;
	private String description;

	// ErrorCode에 지정된 status 필드에 상응하는 HttpStatus 를 반환하는 메서드
	public HttpStatus getHttpStatus(){
		return HttpStatus.valueOf(status);
	}
}
