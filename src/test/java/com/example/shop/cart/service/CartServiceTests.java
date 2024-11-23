package com.example.shop.cart.service;

import com.example.shop.cart.dto.CartItemDTO;
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
}
