package com.example.shop.member.repository;

import com.example.shop.member.entity.MemberEntity;
import com.example.shop.member.exception.MemberExceptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 등록 테스트
    @Test
    public void testInsert() {

        for(int i=1; i<=100; i++) {

            MemberEntity memberEntity = MemberEntity.builder()
                    .mid("user" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .mname("USER" + i)
                    .email("user"+i+"@aaa.com")
                    .role(i<=80 ? "USER" : "ADMIN")
                    .build();

            memberRepository.save(memberEntity);
        }
    }

    // 조회 테스트
    @Test
    public void testRead() {

        String mid = "user10000";

        Optional<MemberEntity> result = memberRepository.findById(mid);

        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

//        System.out.println(MemberExceptions.NOT_FOUND);

        System.out.println(memberEntity);
    }

    // 수정 테스트
    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        String mid = "user1";

        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        memberEntity.changePassword(passwordEncoder.encode("2222"));
        memberEntity.changeName("김민지");
    }

    // 삭제 작업
    @Test
    @Transactional
    @Commit
    public void testDelete() {

        String mid = "user1";

        Optional<MemberEntity> result = memberRepository.findById(mid);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        memberRepository.delete(memberEntity);
    }
}
