package com.kakao.saramaracommunity.security.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.kakao.saramaracommunity.common.response.TokenDto;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class TokenProvider implements InitializingBean {
   private static final String AUTHORITIES_KEY = "auth";

   private final String secret;

   private final long accessTokenValidityInMilliseconds;

   private final long refreshTokenValidityInMilliseconds;
   private Key key;



   public TokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity-in-seconds}")long accessTokenValidityInMilliseconds,
      @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds) {
      this.secret = secret;
      this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
      this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   public String createAccessToken(Authentication authentication){
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = (new Date()).getTime();

      Date accessValidity = new Date(now + this.accessTokenValidityInMilliseconds * 1000);

      String accessToken = Jwts.builder()
              .setSubject(authentication.getName())
              .claim(AUTHORITIES_KEY, authorities)
              .setExpiration(accessValidity)
              .signWith(key, SignatureAlgorithm.HS256)
              .compact();

      return accessToken;
   }

   public String createRefreshToken(Authentication authentication){
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date refreshValidity = new Date(now + this.refreshTokenValidityInMilliseconds * 1000);

      String refreshToken = Jwts.builder()
              .setSubject(authentication.getName())
              .setExpiration(refreshValidity)
              .signWith(key, SignatureAlgorithm.HS256)
              .compact();

      return refreshToken;
   }


   public TokenDto returnToken(Authentication authentication){
      String accessToken = createAccessToken(authentication);
      String refreshToken = createRefreshToken(authentication);

      return TokenDto.builder()
              .grantType("Bearer")
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .build();
   }

   public Authentication getAuthentication(String accessToken) {

      Claims claims = Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(accessToken)
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, /*token*/ accessToken, authorities);
   }



   public boolean validateToken(String token) {
      try {

         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      }
      catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         log.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         log.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         log.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         log.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }

   private Claims parseClaims(String accessToken) {
      try {
         return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
      } catch (ExpiredJwtException e) {
         return e.getClaims();
      }
   }
}
