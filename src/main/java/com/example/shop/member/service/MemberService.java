package com.example.shop.member.service;

import com.example.shop.member.dto.MemberDTO;
import com.example.shop.member.entity.MemberEntity;
import com.example.shop.member.exception.MemberExceptions;
import com.example.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberDTO read(String mid, String mpw) {

        // memberEntity에 존재하는 아이디인지 확인
        Optional<MemberEntity> result = memberRepository.findById(mid);

        // 존재하지않으면 에러 처리
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.BAD_CREDENTIALS::get);

        // 암호화된 패스워드가 일치하는지 확인
        if(!passwordEncoder.matches(mpw, memberEntity.getMpw())) {
            throw MemberExceptions.BAD_CREDENTIALS.get();
        }

        return new MemberDTO(memberEntity);
    }
}
