package com.example.shop.member.security.filter;

import com.example.shop.member.security.auth.CustomUserPrincipal;
import com.example.shop.member.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // JWTCheckFilter가 동작하지 않아야하는 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        
        if(request.getServletPath().startsWith("/api/v1/token/")) {
            return true;
        }

        String path = request.getRequestURI();

        // /api로 시작하지 않는 경로는 필터 동작하지 않도록 설정
        if(!path.startsWith("/api")) {
            return true;
        }
        
        // 경로 지정 필요
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // JWT Exception 처리
        log.info("JWT Check Filter doFilter...........");
        log.info("requestURI: " + request.getRequestURI());

        String headerStr = request.getHeader("Authorization");

        log.info("headerStr: " + headerStr);

        // Access Token이 없는 경우
        if(headerStr == null || !headerStr.startsWith("Bearer ")) {
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));

            return;
        }

        String accessToken = headerStr.substring(7);

        try {
            java.util.Map<String,Object> tokenMap = jwtUtil.validateToken(accessToken);

            // 토큰 검증 결과에 문제가 없을때
            log.info("tokenMap: " + tokenMap);

            String mid = tokenMap.get("mid").toString();

            // 권한 여러 개일때는 ,로 구분해서 처리
            String[] roles = tokenMap.get("role").toString().split(",");

            // 토큰 검증 결과를 이용해서 Authentication 객체를 생성
            UsernamePasswordAuthenticationToken authenticationFilter =
                    new UsernamePasswordAuthenticationToken(
                            new CustomUserPrincipal(mid),
                            null,
                            Arrays.stream(roles)
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .collect(Collectors.toList())
                    );

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationFilter);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\":\"" + e.getMessage() + "\"}");
    }
}