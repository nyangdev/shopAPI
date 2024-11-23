package com.example.shop.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// 장바구니에 아이템을 등록할때 사용되는 DTO
public class AddCartItemDTO {

    private String holder;

    private Long pno;

    private int quantity;
}
