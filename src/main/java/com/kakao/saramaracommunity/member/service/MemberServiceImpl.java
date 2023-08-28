package com.kakao.saramaracommunity.member.service;

import java.time.LocalDateTime;
import java.util.Collections;

import com.kakao.saramaracommunity.member.dto.response.MemberImageDto;
import com.kakao.saramaracommunity.member.dto.response.MemberInfoResDto;
import com.kakao.saramaracommunity.member.dto.response.MemberResDto;
import com.kakao.saramaracommunity.member.exception.MemberErrorCode;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.MemberImage;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import com.kakao.saramaracommunity.member.service.dto.request.ChangePWServiceDto;
import com.kakao.saramaracommunity.member.service.dto.request.SignUpServiceDto;
import jakarta.transaction.Transactional;
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
    public MemberResDto register(SignUpServiceDto signUpDto) {

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
            .memberImage(
                MemberImage.builder().
                    uuid("default").
                    image_name("default").
                    path("default").
                    build()
            )
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
                .memberImageDto(
                    MemberImageDto.from(member.getMemberImage())
                )
                .build();

            MemberResDto response = MemberResDto.builder()
                .success(true)
                .data(currentMemberInfo)
                .build();

            return response;
        } catch (Exception e) {
            MemberResDto response = memberServiceMethod.makeInternalServerErrorResult(e);
            return response;
        }
    }

    // 닉네임 수정
    @Override
    @Transactional
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

            MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
            return response;

        } catch (Exception e) {
            MemberResDto response = memberServiceMethod.makeInternalServerErrorResult(e);
            return response;
        }
    }

    // 비밀번호 수정
    @Override
    public MemberResDto passwordChange(String email, ChangePWServiceDto changePWDto) {
        try {
            // 현재 입력한 비밀번호가 저장된 비밀번호와 일치하지 않는 경우에 대한 예외 처리
            String storedCurrentPw = memberRepository.findByEmail(email).getPassword();
            boolean checkedCurrentPw = memberServiceMethod.checkCurrentPw(
                changePWDto.getCurrentPassword(), storedCurrentPw
            );
            if(!checkedCurrentPw){
                MemberResDto response = MemberResDto.builder()
                    .success(false)
                    .memberErrorCode(MemberErrorCode.NOT_EQUALS_INPUT_CURRENT_PW)
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
                    .memberErrorCode(MemberErrorCode.NOT_EQUALS_INPUT_CHANGED_PW)
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
            MemberResDto response = memberServiceMethod.makeInternalServerErrorResult(e);
            return response;
        }
    }

    // 회원탈퇴
    @Override
    @Transactional
    public MemberResDto unregister(String email) {
        try{
            LocalDateTime now = LocalDateTime.now();
            Member member = memberRepository.findByEmail(email);
            // deleted_at 에 현재 날짜 삽입
            member.checkingDeletedAt(now);

            /*
              1. 랜덤 문자열로 닉네임 교체(@Pattern 에 위배되지 않도록)
              2. 랜덤으로 교체한 닉네임이 DB에 존재하는지 Count
              3. Count 가 1이상인지 0인지 판별 후 중복 여부 판단
              4. 중복된 닉네임이라면 중복되지 않을 때 까지 반복.
              5. 이 때 너무 많은 중복 방지를 위해서 반복 횟수 제한
            */
            String deletedMemberRandomNickName = memberServiceMethod.createRandomNickName();
            long nicknameCount =  memberRepository.countByNickname(deletedMemberRandomNickName);
            boolean changeNicknameForDeleted = memberServiceMethod.isDuplicatedNickname(nicknameCount);

            while(changeNicknameForDeleted){
                int maxCnt = 0;

                deletedMemberRandomNickName = memberServiceMethod.createRandomNickName();
                nicknameCount =  memberRepository.countByNickname(deletedMemberRandomNickName);
                changeNicknameForDeleted = memberServiceMethod.isDuplicatedNickname(nicknameCount);
                maxCnt += 1;

                if(maxCnt == 10){
                    MemberResDto response = MemberResDto.builder()
                        .success(false)
                        .memberErrorCode(MemberErrorCode.INTERNAL_SERVER_ERROR)
                        .build();

                    return response;
                }
            }

            member.changeNickname(deletedMemberRandomNickName);

            MemberResDto response = memberServiceMethod.makeSuccessResultNoData();
            return response;

        }catch(Exception e){
            MemberResDto response = memberServiceMethod.makeInternalServerErrorResult(e);
            return response;
        }
    }
}
