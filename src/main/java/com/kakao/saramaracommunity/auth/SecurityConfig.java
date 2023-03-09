package com.kakao.saramaracommunity.auth;

import com.kakao.saramaracommunity.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @EnableWebSecurity: Spring Security의 설정들을 활성화 시켜준다.
 * authorizeHttpRequests: HTTP URL별로 권한 관리를 위한 옵션의 시작점이다. 해당 옵션을 통해 requestMatchers를 사용할 수 있다.
 * requestMatchers: 권한 관리 대상을 지정하는 옵션이다. URL, HTTP 메소드 별로 관리할 수 있다.
 *      해당 코드에서는 USER 권한을 가진 사용자만 "api/v1/**" 주소를의 API를 호출할 수 있도록 구성하였다.
 * anyRequest: 설정된 값들 이외의 나머지 URL들을 나타낸다.
 *      여기서는 authenticated 옵션을 추가하여 나머지 URL들은 모두 인증된 사용자(로그인된 사용자)만 허용하도록 하였다.
 * logout().logoutSuccessUrl("/"): 로그아웃 기능에 대한 여러 설정의 진입점이다. 로그아웃이 성공한다면 / 주소로 이동한다.
 * oauth2Login: OAuth2 로그인 기능에 관한 설정의 진입점이다.
 * userInfoEndpoint: OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다.
 * userService: 소셜 로그인 성공시 후속 조치를 진행할 UserService의 인터페이스 구현체를 등록한다.
 *      여기서는 소셜 서비스들에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시한다.
 */

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/authenticate", "/ouath2/**","/api/signup", "/api/login", "/api/register").permitAll()
                .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()

                .and()
                    .logout()
                        .logoutSuccessUrl("/")

                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
        return http.build();
    }
}
