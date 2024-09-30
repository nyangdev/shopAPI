package com.example.shop.member.repository;

import com.example.shop.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// MemberEntity를 이용하기 위한 repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
}
