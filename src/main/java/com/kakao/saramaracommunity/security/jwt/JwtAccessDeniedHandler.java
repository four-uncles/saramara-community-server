package com.kakao.saramaracommunity.security.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
   // AccessDeniedHandler 를 implements 해서 403 에러를 반환하는 메서드 재정의
   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      //필요한 권한이 없이 접근하려 할때 403 Forbidden 에러 반환
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
   }
}
