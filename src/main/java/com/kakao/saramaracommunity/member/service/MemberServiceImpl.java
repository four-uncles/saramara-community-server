package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.dto.MemberSaveRequestDto;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberSerivce {
    private final MemberRepository memberRepository;

    @Override
    public Long register(MemberSaveRequestDto requestDto){
        return memberRepository.save(requestDto.toEntity()).getMemberId();
    }
}
