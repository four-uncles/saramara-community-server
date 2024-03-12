package com.kakao.saramaracommunity.member.controller;

import com.kakao.saramaracommunity.common.response.ApiResponse;
import com.kakao.saramaracommunity.member.dto.api.request.MemberCreateRequest;
import com.kakao.saramaracommunity.member.dto.business.response.MemberInfoResponse;
import com.kakao.saramaracommunity.member.service.MemberService;
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
