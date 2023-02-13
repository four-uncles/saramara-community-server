package com.kakao.saramaracommunity.member.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MemberController {
    @GetMapping("/")
    public ResponseEntity<String> index(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return ResponseEntity.ok().body("ok");
//        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}