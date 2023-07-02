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
@Getter
public class MemberResDto<T> {
	boolean success;
	T data;
	ErrorCode status;
}
