package com.kakao.saramaracommunity.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
		Cookie[] cookies = cachedRequest.getCookies();
		if (cookies != null) {
			Cookie session = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("JSEESSIONID"))
				.findFirst().orElse(null);

			if (session != null) {
				session.setHttpOnly(true);
				response.addCookie(session);
			}
		}
		filterChain.doFilter(request, response);
	}
}
