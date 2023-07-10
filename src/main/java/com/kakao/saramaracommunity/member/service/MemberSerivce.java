package com.kakao.saramaracommunity.member.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.member.dto.MemberInfoResDto;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;

@Service
public interface MemberSerivce {
    MemberResDto signUp(SignUpDto requestDto);

    MemberResDto memberInfoChecking(String email);
    MemberResDto nickNameChange(String email, String currentNickname,String changeNickname);
}
