package com.kakao.saramaracommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.kakao.saramaracommunity.security.jwt.JwtAccessDeniedHandler;
import com.kakao.saramaracommunity.security.jwt.JwtAuthenticationEntryPoint;
import com.kakao.saramaracommunity.security.jwt.JwtSecurityConfig;
import com.kakao.saramaracommunity.security.jwt.TokenProvider;
import com.kakao.saramaracommunity.security.oauth.CookieAuthorizationRequestRepository;
import com.kakao.saramaracommunity.security.oauth.CustomOAuth2UserService;
import com.kakao.saramaracommunity.security.oauth.OAuth2AuthenticationFailureHandler;
import com.kakao.saramaracommunity.security.oauth.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .formLogin().disable()
                        .oauth2Login()
                                .authorizationEndpoint()
                                        .baseUri("/oauth2/authorization")
                                        .authorizationRequestRepository(cookieAuthorizationRequestRepository).and()
                                .redirectionEndpoint()
                                        .baseUri("/oauth2/callback/*").and()
                        .userInfoEndpoint()
                                .userService(customOAuth2UserService)
                        .and()
                        .successHandler(authenticationSuccessHandler)
                                .failureHandler(authenticationFailureHandler);

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        /**
         /api/v1/board 로 시작하는 API path의 경우 테스트 진행을 위해 Member의 개발이 완료되고 이후 삭제하여 연관 테스트 진행 예정
         /api/v1/comment 로 시작하는 API path의 경우 테스트 진행을 위해 Member의 개발이 완료되고 이후 삭제하여 연관 테스트 진행 예정
         */
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers( "/favicon.ico","/error", "/api/authenticate","/api/v1/member/**", "/api/login").permitAll()
                .requestMatchers("/auth/**", "/oauth2/**", "/api/v1/board/**", "/api/v1/comment/**", "/api/v1/attach/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }
}