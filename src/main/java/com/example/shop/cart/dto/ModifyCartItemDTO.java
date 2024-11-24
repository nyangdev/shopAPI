package com.example.shop.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyCartItemDTO {

    private Long itemNo;

    private int quantity;

}
