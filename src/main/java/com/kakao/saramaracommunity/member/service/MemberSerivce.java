package com.kakao.saramaracommunity.member.service;

import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.ChangePWDto;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;

@Service
public interface MemberSerivce {

    // 회원가입
    MemberResDto register(SignUpDto requestDto);

    // 회원정보 조회
    MemberResDto memberInfoChecking(String email);

    // 닉네임 변경
    MemberResDto nickNameChange(String email, String currentNickname,String changeNickname);

    // 비밀번호 변경
    MemberResDto passwordChange(String email, ChangePWDto changePWDto);

    // 회원탈퇴
    MemberResDto unregister(String email);

    // 회원복구
}
