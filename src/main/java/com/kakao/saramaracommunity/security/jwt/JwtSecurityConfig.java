package com.kakao.saramaracommunity.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//  TokenProvider, JwtFilter를 SecurityConfig 에 적용할 때 사용할 Config 역할의 클래스
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    // SecurityConfigurerAdapter 상속받아서 Configure 메서드 재정의를 통해서 JwtFilter 를 이용해 주입받은 TokenProvider 를 Security 로직에 필터로 등록
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
            new JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );
    }
}
