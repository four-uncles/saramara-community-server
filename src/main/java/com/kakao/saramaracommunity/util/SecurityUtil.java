package com.kakao.saramaracommunity.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class SecurityUtil {

   // Security Context 에 Authentication 객체가 저장되는 시점은 JwtFilter 의 doFilter 메서드에서 요청이 올 때
   // Security Context 에 Authentication 객체를 저장해서 사용하게 된다.
   // 현재 Security Context 의 Authentication 객체를 이용해서 username 을 반환해주는 메서드
   public static Optional<String> getCurrentUsername() {
      // 현재 Security Context 의 Authentication 객체를 가져온다.
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


      // Authentication 객체가 없을 때
      if (authentication == null) {
         log.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      // Authentication 객체가 있을 때
      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(username);
   }
}
