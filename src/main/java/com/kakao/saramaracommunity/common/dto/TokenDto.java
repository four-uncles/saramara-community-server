package com.kakao.saramaracommunity.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    //private String token;
    private String accessToken;
    private String refreshToken;
    private String grantType; // JWT 인증 탑으로 Bearer 사용


}
