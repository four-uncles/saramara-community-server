package com.kakao.saramaracommunity.member.service;

import java.util.Collections;

import com.kakao.saramaracommunity.member.dto.ChangePWDto;
import com.kakao.saramaracommunity.member.dto.MemberInfoResDto;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;
import com.kakao.saramaracommunity.member.dto.ErrorCode;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberSerivce {
    private final MemberRepository memberRepository;
    private final MemberServiceMethod memberServiceMethod;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Override
    public MemberResDto register(SignUpDto signUpDto) {

        boolean duplicatedEmail = memberServiceMethod.isDuplicatedEmail(
            memberRepository.countByEmail(signUpDto.getEmail())
        );
        boolean duplicatedNickName = memberServiceMethod.isDuplicatedNickname(
            memberRepository.countByNickname(signUpDto.getNickname())
        );

        // 중복된 이메일에 대한 예외처리
        if (duplicatedEmail) {
            MemberResDto response = memberServiceMethod.makeDuplicateEmailResult();
            return response;
        }

        // 중복된 닉네임에 대한 예외처리
        if (duplicatedNickName) {
            MemberResDto response = memberServiceMethod.makeDuplicatedNicknameResult();
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

        MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
        return response;
    }

    // 회원정보 조회
    @Override
    public MemberResDto memberInfoChecking(String email) {
        try {
            Member member = memberRepository.findByEmail(email);

            MemberInfoResDto currentMemberInfo = MemberInfoResDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .type(member.getType())
                .role(member.getRole())
                .build();

            MemberResDto response = MemberResDto.builder()
                .success(true)
                .data(currentMemberInfo)
                .build();

            return response;
        } catch (Exception e) {
            MemberResDto response = memberServiceMethod.internalServerErrorResult(e);
            return response;
        }
    }

    // 닉네임 수정
    @Override
    public MemberResDto nickNameChange(String email, String currentNickname, String changeNickname) {
        try {
            // 변경할 닉네임에 대한 DB 내 중복 여부에 대한 예외처리
            // 중복확인 자체를 count로 하여 자기 자신도 중복으로 하기 때문에 이경우에 대한 처리 필요
            boolean existNickname = memberServiceMethod.isDuplicatedNickname(
                memberRepository.countByNickname(changeNickname));

            // 닉네임 수정 시 중복확인
            if (memberServiceMethod.isChangeNickNameDuplicated(currentNickname, changeNickname, existNickname)) {
                MemberResDto response = memberServiceMethod.makeDuplicatedNicknameResult();
                return response;
            }

            // 더티체킹 방식의 Update
            Member member = memberRepository.findByEmail(email);
            member.changeNickname(changeNickname);
            memberRepository.save(member);

            MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
            return response;

        } catch (Exception e) {
            MemberResDto response = memberServiceMethod.internalServerErrorResult(e);
            return response;
        }
    }

    // 비밀번호 수정
    @Override
    public MemberResDto passwordChange(String email, ChangePWDto changePWDto) {
        try {
            // 현재 입력한 비밀번호가 저장된 비밀번호와 일치하지 않는 경우에 대한 예외 처리
            String storedCurrentPw = memberRepository.findByEmail(email).getPassword();
            boolean checkedCurrentPw = memberServiceMethod.checkCurrentPw(
                changePWDto.getCurrentPassword(), storedCurrentPw
            );
            if(!checkedCurrentPw){
                MemberResDto response = MemberResDto.builder()
                    .success(false)
                    .errorCode(ErrorCode.NOT_EQUALS_INPUT_CURRENT_PW)
                    .build();
                return response;
            }

            // 변경할 비밀번호와 그 비밀번호에 대한 확인 값이 일치하지 않는 경우에 대한 예외 처리
            boolean checkedChangedPw = memberServiceMethod.checkChangedPw(
                changePWDto.getChangedPassword(), changePWDto.getChangedPasswordCheck()
            );
            if(!checkedChangedPw){
                MemberResDto response = MemberResDto.builder()
                    .success(false)
                    .errorCode(ErrorCode.NOT_EQUALS_INPUT_CHANGED_PW)
                    .build();
                return response;
            }

            // 더티체킹 방식의 Update
            Member member = memberRepository.findByEmail(email);
            member.changePassword(
                passwordEncoder.encode(
                    changePWDto.getChangedPassword()
                )
            );
            memberRepository.save(member);
            MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
            return response;

        } catch (Exception e){
            MemberResDto response = memberServiceMethod.internalServerErrorResult(e);
            return response;
        }
    }

    // 회원탈퇴
    @Override
    public MemberResDto unregister(String email) {
        return null;
    }
}
