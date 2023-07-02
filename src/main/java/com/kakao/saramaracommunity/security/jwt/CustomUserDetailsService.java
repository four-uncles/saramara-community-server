package com.kakao.saramaracommunity.security.jwt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

   private final MemberRepository memberRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
      log.info("loadUserByUsername: " + email);

      Optional<Member> existMember = memberRepository.getWithRolesEqualLocal(email);
      if (existMember.isEmpty()) {
         log.info("wef");
         throw new UsernameNotFoundException("등록되지 않은 회원으로 회원가입 처리. ");
      }

      Member authMember = existMember.get();
      List<GrantedAuthority> authorities = authMember.getRole().stream().map(userRole ->
          new SimpleGrantedAuthority("ROLE_" + userRole.name())
      ).collect(Collectors.toList());
      return new User(authMember.getEmail(), authMember.getPassword(),  authorities);
   }

}
