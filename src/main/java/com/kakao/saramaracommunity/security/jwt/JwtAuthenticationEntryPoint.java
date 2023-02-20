package com.kakao.saramaracommunity.security.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   // AuthenticationEntryPoint 를 implements 받아서 401 에러를 반환하는 메서드 재정의
   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      // 유효한 자격증명을 제공하지 않고 접근하려 할때 401에러를 반환한다.

      //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);


       response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
   }
}
