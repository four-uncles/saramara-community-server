package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.service.dto.request.ChangePWServiceDto;
import com.kakao.saramaracommunity.member.service.dto.request.SignUpServiceDto;
import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.response.MemberResDto;

@Service
public interface MemberSerivce {

    // 회원가입
    MemberResDto register(SignUpServiceDto requestDto);

    // 회원정보 조회
    MemberResDto memberInfoChecking(String email);

    // 닉네임 변경
    MemberResDto nickNameChange(String email, String currentNickname,String changeNickname);

    // 비밀번호 변경
    MemberResDto passwordChange(String email, ChangePWServiceDto changePWDto);

    // 회원탈퇴
    MemberResDto unregister(String email);

    // 회원복구
}
