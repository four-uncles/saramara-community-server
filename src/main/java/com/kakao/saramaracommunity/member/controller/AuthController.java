package com.kakao.saramaracommunity.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.common.dto.TokenDto;
import com.kakao.saramaracommunity.member.dto.LoginDto;
import com.kakao.saramaracommunity.security.jwt.JwtFilter;
import com.kakao.saramaracommunity.security.jwt.TokenProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
@RestController

// 공통 URL 설정
@RequestMapping("/api")
public class AuthController {
    // TokenProvider 주입
    private final TokenProvider tokenProvider;

    // AuthenticationManagerBuilder 주입
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // TokenDto 주입
    // private final TokenDto jwt;

    /*public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }*/

    @PostMapping("/authenticate")
    // LoginDto에 대응되는 값들을 POST 요청의 Body 로 받아서 TokenDto 에 작성한 값에 대응 되게 ResponseEntity<TokenDto> 를 반환하는 메서드
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // Spring Security 에서 사용되는 인증용 토큰 객체를 client 로 부터 받은 username, password 를 기반으로 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());


        // Spring Security 에서 사용할 인증 객체 Authentication 을 AuthenticationManagerBuilder 객체와
        // 앞서 만든 UsernamePasswordAuthenticationToken 객체를 이용해서 생성
        // Authentication 객체를 생성하기 위해서 authenticate 메서드가 실행 될 때 CustomUserDetailsService 의 loadUserByUsername 메서드가 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 생성된 Authentication 객체는 SecurityContext 에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Authentication 객체를 이용해서 jwt 값을 생성
        // String jwt = tokenProvider.createToken(authentication);
        TokenDto jwt = tokenProvider.returnToken(authentication);

        // HttpHeaders 객체를 생성하고
        HttpHeaders httpHeaders = new HttpHeaders();
        // HttpHeaders 객체에 JwtFilter 에 작성한 헤더의 이름과 jwt 를 넣어주고
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

        log.info(new ResponseEntity<>(/*new TokenDto(jwt)*/ jwt, httpHeaders, HttpStatus.OK));

        // ResponseEntity 를 이용해 Response 의 Body 에 TokenDto 를 이용해 jwt 를 넣어주고 jwt를 가지고 있는 httpHeaders 와 HttpStatus.OK 를 반환
        return new ResponseEntity<>(/*new TokenDto(jwt)*/ jwt, httpHeaders, HttpStatus.OK);
    }



}
