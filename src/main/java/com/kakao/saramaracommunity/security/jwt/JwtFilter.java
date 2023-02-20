package com.kakao.saramaracommunity.security.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
   public static final String AUTHORIZATION_HEADER = "Authorization";

   // TokenProvider 의존성 주입
   private final TokenProvider tokenProvider;

   // GenericFilterBean 을 상속받아서 doFilter를 JWT 에 맞게 커스텀
   // 실제 필터링 로직은 doFilter 메서드가 수행한다.
   // doFilter 메서드는 인증에 대한 정보를 현재 실행중인 SecurityContext 에 저장하는 역할을 한다.
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

      // Client 의 Request 정보를 받는 객체
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

      // 아래의 resolveToken 메서드로 부터 가져온 Client 의 jwt
      String jwt = resolveToken(httpServletRequest);

      // Request 정보중 Request 의 주소
      String requestURI = httpServletRequest.getRequestURI();

      // resolveToken 메서드로 가져온 jwt의 유효성 검증을 하고
      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
         // 토큰이 정상이면 jwt 로부터 Authentication 객체를 가져와서 SecurityContext 에 set 하게 된다.
         Authentication authentication = tokenProvider.getAuthentication(jwt);
         SecurityContextHolder.getContext().setAuthentication(authentication);
         log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
      } else {
         log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   // Request Header 의 정보를 받기 위해서 Request를  RequestServlet 객체로 매개변수로 받기 위한 메서드
   // Request Header 중에서 Bearer 를 제외한 키값만 가져오는 메서드

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
         return bearerToken.substring(7);
      }

      return null;
   }
}
