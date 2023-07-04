package com.kakao.saramaracommunity.member.service;

import java.util.Collections;

import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;
import com.kakao.saramaracommunity.member.dto.ErrorCode;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberSerivce {
    private final MemberRepository memberRepository;
    private final MemberServiceMethod memberServiceMethod;
    private final PasswordEncoder passwordEncoder;

    @Override

    public MemberResDto signUp(SignUpDto signUpDto){

        boolean duplicatedEmail = memberServiceMethod.emailDuplicated(
            memberRepository.countByEmail(signUpDto.getEmail())
        );
        boolean duplicatedNickName = memberServiceMethod.nickNameDuplicated(
            memberRepository.countByNickname(signUpDto.getNickname())
        );

        if(duplicatedEmail){
           MemberResDto response = MemberResDto.builder()
                .success(false)
                .status(ErrorCode.DUPLICATE_EMAIL)
                .build();
            return response;
        }

        if(duplicatedNickName){
            MemberResDto response = MemberResDto.builder()
                .success(false)
                .status(ErrorCode.DUPLICATE_NICKNAME)
                .build();
            return response;
        }

        Member member = Member.builder()
            .email(signUpDto.getEmail())
            .password(passwordEncoder.encode(signUpDto.getPassword()))
            .nickname(signUpDto.getNickname())
            .type(Type.LOCAL)
            .role(Collections.singleton(Role.USER))
            .picture("폴킴이 부릅니다 비~")
            .build();

        memberRepository.save(member);

        MemberResDto response = MemberResDto.builder()
            .success(true)
            .build();
        return response;
    }
}
