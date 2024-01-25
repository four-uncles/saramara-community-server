// package com.kakao.saramaracommunity.filter;
//
// import java.io.IOException;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import org.springframework.web.util.ContentCachingRequestWrapper;
// import org.springframework.web.util.ContentCachingResponseWrapper;
//
// import com.kakao.saramaracommunity.member.repository.MemberRepository;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class LoginFilter extends OncePerRequestFilter {
//
// 	private final MemberRepository memberRepository;
//
// 	@Override
// 	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
// 		FilterChain filterChain) throws ServletException, IOException {
// 		ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
// 		ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
//
// 		HttpSession session = cachedRequest.getSession();
//
// 		if (!cachedRequest.getRequestURI().equals("/api/v1/member/login")) {
// 			if (session == null || session.getAttribute("member") == null) {
// 				throw new RuntimeException("비인가 회원접근");
// 			}
//
// 			if (!memberRepository.existsMemberByEmail(session.getAttribute("member").toString())) {
// 				throw new RuntimeException("비인가 회원접근");
// 			}
// 		}
//
// 		filterChain.doFilter(request, response);
// 	}
// }
