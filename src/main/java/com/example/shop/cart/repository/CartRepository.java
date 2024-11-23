package com.example.shop.cart.repository;

import com.example.shop.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    // 소유주로 CartEntity 존재하는지 검색
    Optional<CartEntity> findByHolder(String holder);
}
