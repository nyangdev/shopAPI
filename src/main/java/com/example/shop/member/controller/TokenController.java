package com.example.shop.member.controller;

import com.example.shop.member.dto.MemberDTO;
import com.example.shop.member.security.util.JWTUtil;
import com.example.shop.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader("Authorization") String accessTokenStr,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid
    ) {
        // 넘어오는 값 검증
        // 알맞지 않은 값이면 400 Bad Request 넘기기
        log.info("access token with Bearer.............................." + accessTokenStr);

        // accessTokenStr 검증
        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")) {
            return handleException("No Access Token", 400);
        }

        // refreshToken 검증
        if(refreshToken == null) {
            return handleException("No Refresh Token", 400);
        }

        log.info("refresh token.............." + refreshToken);

        // mid 검증
        if(mid == null) {
            return handleException("No MID", 400);
        }

        // Access Token이 만료되었는지 확인
        // Bearer 자르기
        String accessToken = accessTokenStr.substring(7);

        try {
            jwtUtil.validateToken(accessToken);

            // 아직 만료기한이 남았을때
            Map<String, String> data = makeData(mid, accessToken, refreshToken);

            log.info("Access Token is not expired.................");

            return ResponseEntity.ok(data);

        } catch (ExpiredJwtException expiredJwtException) {
            // Refresh가 필요한 상황
            try {
                Map<String, String> newTokenMap = makeNewToken(mid, refreshToken);

                return ResponseEntity.ok(newTokenMap);
            } catch (Exception e) {
                return handleException("REFRESH " + e.getMessage(), 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return handleException(e.getMessage(), 400);
        }

        // Refresh Token 검증

        // Refresh Token에서 mid값 추출

        // 새로운 Access Token, Refresh Token 생성

        // 전송
    }

    // 에러 예외 메시지 처리
    private ResponseEntity<Map<String, String>> handleException(String msg, int status) {
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    // 만료되지 않았는데 호출될때는 파라미터로 전달받은 정보를 그대로 전송하도록 구성
    // 이를 위한 메서드
    private Map<String, String> makeData(String mid, String accessToken, String refreshToken) {
        return Map.of("mid", mid, "accessToken", accessToken, "refreshToken", refreshToken);
    }

    // 새로운 토큰 발행 메서드
    // Refresh Token을 검증하고 새로운 토큰을 발행한다
    private Map<String, String> makeNewToken(String mid, String refreshToken) {

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        log.info("refresh token claims: " + claims);

        if(!mid.equals(claims.get("mid").toString())) {
            throw new RuntimeException("Invalid Refresh Token Host");
        }

        // mid를 이용해서 사용자 정보를 다시 확인한 후에 새로운 토큰 생성
        MemberDTO memberDTO = memberService.getByMid(mid);

        Map<String, Object> newClaims = memberDTO.getDataMap();

        String newAccessToken = jwtUtil.createToken(newClaims, 10);

        String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        return makeData(mid, newAccessToken, newRefreshToken);
    }
}
