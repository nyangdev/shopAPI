package com.example.shop.cart.repository;

import com.example.shop.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    // 장바구니 아이템 조회
    @Query("select c " +
            " from CartItemEntity c " +
            " join fetch c.product " +
            " where c.cart.holder = :holder" +
            " order by c.itemNo desc "
    )
    Optional<List<CartItemEntity>> getCartItemsOfHolder(@Param("holder") String holder);
}
