package com.kakao.saramaracommunity.member.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.member.dto.ChangePWDto;
import com.kakao.saramaracommunity.member.dto.MemberResDto;
import com.kakao.saramaracommunity.member.dto.SignUpDto;
import com.kakao.saramaracommunity.member.service.MemberSerivce;
import com.kakao.saramaracommunity.member.service.MemberServiceMethod;
import com.kakao.saramaracommunity.member.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final UserService userService;
    private final MemberSerivce memberSerivce;
    private final MemberServiceMethod memberServiceMethod;

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<MemberResDto> signup(@Valid @RequestBody SignUpDto signUpDto) {
        MemberResDto response = memberSerivce.signUp(signUpDto);
        HttpStatus status = memberServiceMethod.changeStatus(response);
        return new ResponseEntity<>(response, status);
    }

    // 회원정보 조회
    @GetMapping("/member/{email}")
    public ResponseEntity<MemberResDto> memberInfoChecking(
        @Valid @PathVariable @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")String email
    ) {
        MemberResDto response = memberSerivce.memberInfoChecking(email);
        HttpStatus status = memberServiceMethod.changeStatus(response);
        return new ResponseEntity<>(response, status);
    }

    // 닉네임 수정
    @PutMapping("/member/{email}/{currentNickname}/{changeNickname}")
    public ResponseEntity<MemberResDto> nicknameChanging(
        @Valid @PathVariable @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")String email,
        @Valid @PathVariable @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9-_]{2,10}$")String currentNickname,
        @Valid @PathVariable @Pattern(regexp = "^[ㄱ-ㅎ가-힣A-Za-z0-9-_]{2,10}$")String changeNickname
        ){
        MemberResDto response = memberSerivce.nickNameChange(email, currentNickname, changeNickname);
        HttpStatus status = memberServiceMethod.changeStatus(response);
        return new ResponseEntity<>(response, status);
    }

    // 비밀번호 수정
    @PutMapping("/member/password/{email}")
    public ResponseEntity<MemberResDto> passwordChange(
        @Valid @PathVariable String email,
        @Valid @RequestBody ChangePWDto changePWDto
    ){
        MemberResDto response = memberSerivce.passwordChange(email, changePWDto);
        HttpStatus status = memberServiceMethod.changeStatus(response);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SignUpDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());

    }

    @GetMapping("/user/{nickname}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SignUpDto> getUserInfo(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(nickname));
    }


    @GetMapping("/")
    public ResponseEntity<String> index(Authentication authentication) {
        log.info("authentication: " + authentication);
        return ResponseEntity.ok().body(String.valueOf(authentication));
    }
}
