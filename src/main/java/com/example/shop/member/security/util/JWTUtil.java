package com.example.shop.member.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component // 스프링의 Bean이 되도록 구성
@Log4j2
public class JWTUtil {
    private static String key = "1234567890123456789012345678901234567890";

    // JWT 문자열 생성 메서드
    public String createToken(Map<String, Object> valueMap, int min) {

        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder().header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .expiration((Date.from(ZonedDateTime.now()
                        .plusMinutes(min).toInstant()))).claims(valueMap)
                .signWith(key)
                .compact();
    }

    // JWT 문자열 검증
    // payload 반환
    public Map<String, Object> validateToken(String token) {

        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Claims claims = Jwts.parser().verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        log.info("claims: " + claims);

        return claims;
    }
}
