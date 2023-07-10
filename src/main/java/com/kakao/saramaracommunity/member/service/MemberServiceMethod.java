package com.kakao.saramaracommunity.member.service;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.ErrorCode;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceMethod {

	// 회원가입 - Email 중복확인, 로그인 - 잘못된 Email
	public boolean emailDuplicated(long emailCount){
		try{
			if(emailCount == 1l){
				return true;
			} else {
				return false;
			}
		} catch (Exception e){
			log.error(e.getMessage());
			return true;
		}
	}

	// NickName 중복 확인
	public boolean nickNameDuplicated(long nicknameCount){
		try{
			if(nicknameCount == 1l){
				return true;
			} else {
				return false;
			}
		} catch (Exception e){
			log.error(e.getMessage());
			return true;
		}
	}

	// HttpStatus.OK가 아닌 경우의 응답에 대해서 대응하는 ErrorCode의 HttpStatus로 변경하는 메서드
	public HttpStatus changeStatus(MemberResDto memberResDto) {
		try {
			if (!memberResDto.isSuccess()){
				return memberResDto.getErrorCode().getHttpStatus();
			}
			return HttpStatus.OK;
		} catch (Exception e){
			log.warn(e.getMessage());
			return HttpStatus.OK;
		}
	}
}
