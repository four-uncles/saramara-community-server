package com.kakao.saramaracommunity.member.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.controller.dto.request.SignUpDto;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpDto signup(SignUpDto signUpDto) {

        Member user = Member.builder()
            .email(signUpDto.getEmail())
            .password(passwordEncoder.encode(signUpDto.getPassword()))
            .nickname(signUpDto.getNickname())
            .type(Type.LOCAL)
            .role(Collections.singleton(Role.USER))
            .build();

        return SignUpDto.from(memberRepository.save(user));
    }

    @Transactional(readOnly = true)
    public SignUpDto getUserWithAuthorities(String email) {
        return SignUpDto.from(memberRepository.getWithRolesEqualLocal(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public SignUpDto getMyUserWithAuthorities() {
        return SignUpDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::getWithRoles)
                .orElseThrow()
        );
    }
}
