package com.example.shop.cart.entity;

import com.example.shop.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

// 장바구니에 있는 상품과 수량 엔티티
// 장바구니 안에 대한 엔티티.
@Entity
@Table(name = "tbl_cart_items", indexes = @Index(columnList = "cart_cno"))
@Getter
@ToString(exclude = {"product", "cart"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemNo;

    // 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private CartEntity cart;

    // 수량 변경
    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
