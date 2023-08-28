package com.kakao.saramaracommunity.member.dto.response;

import com.kakao.saramaracommunity.member.exception.MemberErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MemberResDto<T> {
	boolean success;
	T data;
	MemberErrorCode memberErrorCode;
}
