package com.kakao.saramaracommunity.member.dto;

import lombok.Getter;

@Getter
public class OAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
}
