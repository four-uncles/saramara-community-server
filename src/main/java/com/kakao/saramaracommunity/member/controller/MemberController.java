package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.auth.dto.OAuthAttributes;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MemberController {
    @GetMapping("/")
    public ResponseEntity<String> index(Authentication authentication) {
        System.out.println("authentication: " + authentication);
        return ResponseEntity.ok().body(String.valueOf(authentication));
//        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}