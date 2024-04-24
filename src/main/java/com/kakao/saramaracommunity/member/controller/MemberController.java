package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.dto.api.request.MemberCreateRequest;
import com.kakao.saramaracommunity.member.dto.business.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

	@Operation(summary = "회원 가입")
	@PostMapping
	public ResponseEntity<ApiResponse> registerMember (
			@RequestBody @Valid MemberCreateRequest request
	) {
		memberService.createMember(request.toServiceRequest());
		return ResponseEntity.ok().body(
			ApiResponse.successResponse(
				OK,
				"성공적으로 회원가입을 완료하였습니다."
			)
		);
	}

	@Operation(summary = "회원 조회")
	@GetMapping("/{email}")
	public ResponseEntity<ApiResponse> getMemberInfo (
			@PathVariable("email") String email
	) {
		MemberInfoResponse response = memberService.getMemberInfo(email);
		return ResponseEntity.ok().body(
			ApiResponse.successResponse(
				OK,
				"성공적으로 회원의 프로필 정보를 조회하였습니다.",
				response
			)
		);
	}
}
