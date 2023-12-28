package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;

public class MemberFixture {

    public static Member createMember(String email, String nickname) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password("test")
                .type(Type.LOCAL)
                .build();
    }

}
