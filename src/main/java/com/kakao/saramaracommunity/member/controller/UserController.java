package com.kakao.saramaracommunity.member.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.member.dto.SecurityMemberDto;
import com.kakao.saramaracommunity.member.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    @PostMapping("/signup")
    public ResponseEntity<SecurityMemberDto> signup(@Valid @RequestBody SecurityMemberDto securityMemberDto) {
        return ResponseEntity.ok(userService.signup(securityMemberDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SecurityMemberDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());

    }

    @GetMapping("/user/{nickname}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SecurityMemberDto> getUserInfo(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(nickname));
    }


    @GetMapping("/")
    public ResponseEntity<String> index(Authentication authentication) {
        log.info("authentication: " + authentication);
        return ResponseEntity.ok().body(String.valueOf(authentication));
    }
}
