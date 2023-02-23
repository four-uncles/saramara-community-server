package com.kakao.saramaracommunity.member.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.exception.DuplicateMemberException;
import com.kakao.saramaracommunity.member.exception.NotFoundMemberException;
import com.kakao.saramaracommunity.member.dto.SecurityMemberDto;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.persistence.MemberRepository;
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
    public SecurityMemberDto signup(SecurityMemberDto securityMemberDto) {

        if (memberRepository.getWithRolesEqualLocal(securityMemberDto.getEmail()).orElse(null) != null) {
            log.warn("시발");
            throw new DuplicateMemberException("젭잘 불탁할겡");
        }

        Member user = Member.builder()
            .email(securityMemberDto.getEmail())
            .password(passwordEncoder.encode(securityMemberDto.getPassword()))
            .nickname(securityMemberDto.getNickname())
            .type(Type.LOCAL)
            .role(Collections.singleton(Role.USER))
            .picture(securityMemberDto.getPicture())
            .build();


        return SecurityMemberDto.from(memberRepository.save(user));
    }

    @Transactional(readOnly = true)
    public SecurityMemberDto getUserWithAuthorities(String email) {
        return SecurityMemberDto.from(memberRepository.getWithRolesEqualLocal(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public SecurityMemberDto getMyUserWithAuthorities() {
        SecurityMemberDto member_not_found = SecurityMemberDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::getWithRoles)
                .orElseThrow(() -> NotFoundMemberException.builder().message("Member not found").build())
        );
        return member_not_found;
    }
}
