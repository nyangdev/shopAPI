package com.example.shop.cart.service;

import com.example.shop.cart.dto.CartItemDTO;
import com.example.shop.cart.entity.CartItemEntity;
import com.example.shop.cart.repository.CartItemRepository;
import com.example.shop.cart.repository.CartRepository;
import com.example.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CartItemDTO> getAllItems(String mid) {

        List<CartItemDTO> itemDTOList = new ArrayList<>();

        Optional<List<CartItemEntity>> result = cartItemRepository.getCartItemsOfHolder(mid);

        // 소유주의 장바구니가 비어있을 경우에
        // ArrayList 반환
        if(result.isEmpty()) {
            return itemDTOList;
        }

        List<CartItemEntity> cartItemEntityList = result.get();

        cartItemEntityList.forEach(cartItemEntity -> {
            itemDTOList.add(entityToDTO(cartItemEntity));
        });
        return itemDTOList;
    }

    private CartItemDTO entityToDTO(CartItemEntity cartItemEntity) {
        return CartItemDTO. builder()
                .itemNo(cartItemEntity.getItemNo())
                .pname(cartItemEntity.getProduct().getPname())
                .pno(cartItemEntity.getProduct().getPno())
                .price(cartItemEntity.getProduct().getPrice())
                .image(cartItemEntity.getProduct().getImages().first().getFileName())
                .quantity(cartItemEntity.getQuantity())
                .build();
    }
}
