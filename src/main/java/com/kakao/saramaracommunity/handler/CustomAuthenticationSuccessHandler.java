package com.kakao.saramaracommunity.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final MemberService memberService;
	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		log.info("authentication = " + authentication);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		MemberInfoResponse authenticatedMember = memberService.getMemberInfoByEmail(authentication.getName());
		ApiResponse<Object> bodyJson = ApiResponse.of(HttpStatus.OK, "로그인에 성공하셨습니다.", authenticatedMember.email());

		String body = objectMapper.writeValueAsString(bodyJson);

		response.getWriter().write(body);
	}
}
