package com.kakao.saramaracommunity.member.dto.business.request;

public record MemberCreateServiceRequest(
        String email,
        String password,
        String nickname
) {
    public static MemberCreateServiceRequest of(
            String email,
            String password,
            String nickname
    ) {
        return new MemberCreateServiceRequest(
                email,
                password,
                nickname
        );
    }
}
