package com.example.shop.cart.service;

import com.example.shop.cart.dto.AddCartItemDTO;
import com.example.shop.cart.dto.CartItemDTO;
import com.example.shop.cart.dto.ModifyCartItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Test
    public void testGetCartList() {
        String mid = "user22";

        List<CartItemDTO> cartList = cartService.getAllItems(mid);

        cartList.forEach(cartItemDTO -> {
            System.out.println(cartItemDTO);
        });
    }

    // 장바구니 아이템 추가 테스트
    @Test
    public void testRegisterItem() {
        String mid = "user55";
        Long pno = 40L;
        int qty = 2;

        AddCartItemDTO addCartItemDTO = AddCartItemDTO.builder()
                .holder(mid)
                .pno(pno)
                .quantity(qty)
                .build();

        cartService.registerItem(addCartItemDTO);
    }

    @Test
    public void testModifyItem() {

        Long itemNo = 1L;
        int qty = 0;

        ModifyCartItemDTO modifyCartItemDTO = ModifyCartItemDTO.builder()
                .itemNo(itemNo)
                .quantity(qty)
                .build();

        cartService.modifyItem(modifyCartItemDTO);
    }
}
