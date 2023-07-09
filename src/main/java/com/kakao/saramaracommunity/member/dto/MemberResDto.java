package com.kakao.saramaracommunity.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class MemberResDto<T> {
	@Getter
	boolean success;
	T data;
	@Getter
	ErrorCode status;
}
