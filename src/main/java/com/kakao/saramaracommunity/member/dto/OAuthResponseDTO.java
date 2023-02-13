package com.kakao.saramaracommunity.member.dto;

import lombok.Getter;

@Getter
public class OAuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
}
