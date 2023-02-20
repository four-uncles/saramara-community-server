package com.kakao.saramaracommunity.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.common.dto.TokenDto;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerTest {

    /*private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody String accessToken) {
        return ResponseEntity.ok().body(authService.refreshToken(request, response, accessToken));
    }*/

    @GetMapping(value = "/token")
    public ResponseEntity token(@RequestParam String accessToken,
                                @RequestParam String refreshToken) {

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

        return new ResponseEntity<>( tokenDto, HttpStatus.OK);

    }
}
