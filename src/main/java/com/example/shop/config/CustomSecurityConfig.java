package com.example.shop.config;

import com.example.shop.member.security.filter.JWTCheckFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private JWTCheckFilter jwtCheckFilter;

    @Autowired
    private void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter) {
        this.jwtCheckFilter = jwtCheckFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        log.info("filter chain......");

        // 로그인 페이지와 관련된 설정
        // 로그인 페이지를 제공하지 않도록 함
        httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.disable();
        });

        // logout() 해당 사항 없으므로 미사용 설정
        httpSecurity.logout(config -> config.disable());
        
        httpSecurity.csrf(config -> { config.disable(); });
        
        // API 서버는 무상태로 유지하도록 구성하고
        // 세션을 생성하지 않도록 구성한다
        httpSecurity.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        // jwtCheckFilter를 UsernamePasswordAuthenticationFilter 앞에 두기
        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}