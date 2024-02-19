package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;
import com.kakao.saramaracommunity.member.controller.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<ApiResponse> registerMember (
			@RequestBody MemberRegisterRequest request
	) {
		memberService.registerMember(request);
		return ResponseEntity.ok().body(
			ApiResponse.successResponse(
				OK,
				"성공적으로 회원가입이 되었습니다."
			)
		);
	}

	@GetMapping("/{email}")
	public ResponseEntity<ApiResponse> getMemberInfo (
			@PathVariable("email") String email
	) {
		MemberInfoResponse response = memberService.getMemberInfoByEmail(email);
		return ResponseEntity.ok().body(
			ApiResponse.successResponse(
				OK,
				"해당 이메일에 대한 회원정보가 성공적으로 조회되었습니다.",
				response
			)
		);
	}
}
