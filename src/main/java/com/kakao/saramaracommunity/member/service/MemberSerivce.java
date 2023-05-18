package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.dto.MemberSaveRequestDto;

public interface MemberSerivce {
    Long register(MemberSaveRequestDto requestDto);
}
