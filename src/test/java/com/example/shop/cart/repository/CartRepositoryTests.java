package com.example.shop.cart.repository;

import com.example.shop.cart.entity.CartEntity;
import com.example.shop.cart.entity.CartItemEntity;
import com.example.shop.product.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Test
    @Commit
    public void testInsertCart() {
        String mid = "user22";
        Long pno = 60L;
        int qty = 5;

        Optional<CartEntity> cartResult = cartRepository.findByHolder(mid);

        CartEntity cartEntity = cartResult.orElseGet(() -> {

            CartEntity cart = CartEntity.builder().holder(mid).build();

            return cartRepository.save(cart);
        });

        ProductEntity productEntity = ProductEntity.builder().pno(pno).build();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .cart(cartEntity)
                .product(productEntity)
                .quantity(qty)
                .build();

        cartItemRepository.save(cartItemEntity);
    }

    @Test
    public void testRead() {
        String mid = "user22";

        Optional<List<CartItemEntity>> result = cartItemRepository.getCartItemsOfHolder(mid);

        List<CartItemEntity> cartItemEntityList = result.orElse(null);

        cartItemEntityList.forEach(cartItemEntity -> {
           System.out.println(cartItemEntity);
           System.out.println(cartItemEntity.getProduct());
//           System.out.println(cartItemEntity.getProduct().getImages());
           System.out.println("----------------------------------------------");
        });
    }

    @Test
    @Transactional
    @Commit
    public void testModifyCartItem() {
        Long itemNo = 2L;
        int qty = 0;

        Optional<CartItemEntity> result = cartItemRepository.findById(itemNo);

        CartItemEntity cartItemEntity = result.get();

        cartItemEntity.changeQuantity(qty);

        if(cartItemEntity.getQuantity() <= 0) {
            cartItemRepository.delete(cartItemEntity);
        }
    }
}
