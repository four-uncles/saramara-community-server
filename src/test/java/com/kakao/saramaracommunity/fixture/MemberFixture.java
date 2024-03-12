package com.kakao.saramaracommunity.fixture;

import com.kakao.saramaracommunity.member.entity.Member;

public enum MemberFixture {

    NORMAL_MEMBER_LANGO(
            "lango@test.com",
            "Lango1234!@",
            "lango"
    ),
    NORMAL_MEMBER_SONNY(
            "sonny@test.com",
            "Sonny1234@",
            "sonny"
    ),
    NORMAL_MEMBER_WOOGI(
            "gitshineit@test.com",
            "Woogi999##",
            "woogi"
    )
    ;

    private final String email;
    private final String password;
    private final String nickname;

    MemberFixture(
            String email,
            String password,
            String nickname
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getNickname() { return nickname; }

    public Member createMember() {
        return Member.builder()
                .email(getEmail())
                .password(getPassword())
                .nickname(getNickname())
                .build();
    }

}
