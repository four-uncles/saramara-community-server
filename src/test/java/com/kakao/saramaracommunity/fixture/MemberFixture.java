package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.member.entity.Member;

public class MemberFixture {

    public static Member createMember(String email, String nickname) {
        return Member.builder()
                .email(email)
                .password("test")
                .nickname(nickname)
                .build();
    }

}
