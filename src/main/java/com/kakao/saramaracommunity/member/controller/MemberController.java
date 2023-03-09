package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.member.dto.MemberSaveRequestDto;
import com.kakao.saramaracommunity.member.service.MemberSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberSerivce memberSerivce;

    @GetMapping("/")
    public ResponseEntity<String> index(Authentication authentication) {
        System.out.println("authentication: " + authentication);
        return ResponseEntity.ok().body(String.valueOf(authentication));
    }

    @PostMapping("/api/register")
    public ResponseEntity<Long> register(@RequestBody MemberSaveRequestDto requestDto) {
        Long memberId = memberSerivce.register(requestDto);
        return ResponseEntity.ok().body(memberId);
    }
}