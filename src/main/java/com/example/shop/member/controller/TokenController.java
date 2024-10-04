package com.example.shop.member.controller;

import com.example.shop.member.dto.MemberDTO;
import com.example.shop.member.security.util.JWTUtil;
import com.example.shop.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/token")
@Log4j2
@RequiredArgsConstructor
public class TokenController {

    // 실제 사용자 정보를 확인해서 토큰을 발행하는 컨트롤러

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody MemberDTO memberDTO) {

        log.info("make token............");
        
        // 존재하는 사용자인지 확인
        MemberDTO memberDTOResult = memberService.read(memberDTO.getMid(), memberDTO.getMpw());

        log.info(memberDTOResult);

        String mid = memberDTOResult.getMid();

        Map<String, Object> dataMap = memberDTOResult.getDataMap();

        String accessToken = jwtUtil.createToken(dataMap, 10);

        String refreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }
}
