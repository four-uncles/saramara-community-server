package com.kakao.saramaracommunity.member.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        return ResponseEntity.ok("You are logged in as " + authentication.getName());
    }
}
