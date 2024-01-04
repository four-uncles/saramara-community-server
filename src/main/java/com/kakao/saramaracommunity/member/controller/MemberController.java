package com.kakao.saramaracommunity.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerMember (@RequestBody MemberRegisterRequest request) {
		memberService.registerMember(request);

		return ResponseEntity.ok().body(
			ApiResponse.of(
				HttpStatus.OK,
				"성공적으로 회원가입이 되었습니다."
			)
		);
	}
}