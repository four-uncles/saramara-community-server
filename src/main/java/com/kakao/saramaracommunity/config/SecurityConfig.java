package com.kakao.saramaracommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.filter.LoginAuthenticationFilter;
import com.kakao.saramaracommunity.handler.CustomAuthenticationSuccessHandler;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	private final MemberService memberService;

	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

		AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

		managerBuilder.userDetailsService(userDetailsService);

		AuthenticationManager authenticationManager = managerBuilder.build();

		httpSecurity.authenticationManager(authenticationManager);

		httpSecurity
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable) // csrf disabled
			.addFilterAt(
				this.abstractAuthenticationProcessingFilter(authenticationManager,
					authenticationSuccessHandler()),
				UsernamePasswordAuthenticationFilter.class
			)
			.formLogin(AbstractHttpConfigurer::disable)// mvc formLogin disabled
			.sessionManagement(sessionManagementConfigurer -> // 세션 정책 적용
				sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.sessionFixation(
						SessionManagementConfigurer.SessionFixationConfigurer::newSession) // session rotate 허용
					.maximumSessions(1)) // 세션 쿠키는 한 개만 허용
			.logout(out -> // 로그아웃 정책 적용
				out.logoutUrl("/api/v1/member/logout")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSONID")
					.logoutSuccessHandler(
						((request, response, authentication) -> SecurityContextHolder.clearContext())))
			.headers(
				httpSecurityHeadersConfigurer ->
					httpSecurityHeadersConfigurer
						.frameOptions(
							HeadersConfigurer.FrameOptionsConfig::sameOrigin
						)
			);

		return httpSecurity.build();
	}

	@Bean
	public DelegatingSecurityContextRepository delegatingSecurityContextRepository() {
		return new DelegatingSecurityContextRepository(
			new RequestAttributeSecurityContextRepository(),
			new HttpSessionSecurityContextRepository()
		);
	}

	public AbstractAuthenticationProcessingFilter abstractAuthenticationProcessingFilter(final AuthenticationManager authenticationManager,
		final AuthenticationSuccessHandler authenticationSuccessHandler) {
		return new LoginAuthenticationFilter(
			"/api/v1/member/login",
			authenticationManager,
			authenticationSuccessHandler
		);
	}

	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler(memberService, objectMapper);
	}
}
