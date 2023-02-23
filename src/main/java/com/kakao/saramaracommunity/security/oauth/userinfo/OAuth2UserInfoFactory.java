package com.kakao.saramaracommunity.security.oauth.userinfo;

import java.util.Map;

import com.kakao.saramaracommunity.member.entity.Type;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(Type authType, Map<String, Object> attributes) {
        switch (authType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
