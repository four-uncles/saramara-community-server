package com.kakao.saramaracommunity.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.member.dto.api.request.MemberLoginRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


	public LoginAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
		AuthenticationSuccessHandler authenticationSuccessHandler) {
		super(defaultFilterProcessesUrl, authenticationManager);

		setSecurityContextRepository(
			new DelegatingSecurityContextRepository(
				new HttpSessionSecurityContextRepository(),
				new RequestAttributeSecurityContextRepository()
			)
		);

		setAuthenticationSuccessHandler(authenticationSuccessHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException, ServletException, IOException {
		log.info("로그인 시도합니다.");
		String method = request.getMethod();
		if (!method.equals("POST")) {
			throw new AuthenticationServiceException("인증 요청에 대해서는 POST HTTP METHOD 사용이 필수입니다.");
		}
		try {
			ServletInputStream inputStream = request.getInputStream();
			MemberLoginRequest memberLoginRequest = new ObjectMapper().readValue(inputStream, MemberLoginRequest.class);
			log.info(memberLoginRequest);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginRequest.email(), memberLoginRequest.password());
			log.info(authenticationToken);
			return this.getAuthenticationManager().authenticate(authenticationToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
