package com.kakao.saramaracommunity.member.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.exception.DuplicateMemberException;
import com.kakao.saramaracommunity.exception.NotFoundMemberException;
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

    // 회원가입 메서드
    // 회원가입 시 사용자가 입력한 정보에 대응되는 UserDto 를 받아 DB에서 User의 정보와 해당 User의 정보가 존재하는지 확인하는데
    // JPA 에서 제공하는 쿼리 메서드는 두 개 이상의 테이블에서 작업을 하는 경우
    // 각 각 의 테이블에 커넥션해서 작업하게 되는데 JPA 에서는 한번에 하나의 커넥션만 수행하기 때문에 트랜젝션 설정을 하지 않는다면 에러가 발생하게 된다.

    @Transactional
    public SecurityMemberDto signup(SecurityMemberDto securityMemberDto) {
        // 이미 입력한 정보에 대한 회원이 있을 때 DuplicateMemberException 예외 발생
        if (memberRepository.getWithRolesEqualLocal(securityMemberDto.getEmail()).orElse(null) != null) {
            log.warn("시발");
            throw new DuplicateMemberException("젭잘 불탁할겡");
        }


        // 중복된 정보의 사용자 정보와 사용자에 대한 권한 정보가 없다면
        // 해당 사용자에게 USER 권한 부여
        // 사용자 정보를 DB 에 저장
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

    // email 을 매개변수로 사용자 정보와 권한 정보를 가져오는 메서드인데
    @Transactional(readOnly = true)
    public SecurityMemberDto getUserWithAuthorities(String email) {
        return SecurityMemberDto.from(memberRepository.getWithRolesEqualLocal(email).orElse(null));
    }

    // 현재 Security Context 에 저장된 username 의 사용자 정보, 권한정보 만을 가져오는 메서드
    @Transactional(readOnly = true)
    public SecurityMemberDto getMyUserWithAuthorities() {
        SecurityMemberDto member_not_found = SecurityMemberDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::getWithRoles)
                //.flatMap(memberRepository::getWithRolesEqualLocal)
                //.flatMap(memberRepository::getWithRolesEqualOAuth)
                .orElseThrow(() -> NotFoundMemberException.builder().message("Member not found").build())
        );
        return member_not_found;
    }
}
