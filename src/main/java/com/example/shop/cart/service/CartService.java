package com.example.shop.cart.service;

import com.example.shop.cart.dto.AddCartItemDTO;
import com.example.shop.cart.dto.CartItemDTO;
import com.example.shop.cart.dto.ModifyCartItemDTO;
import com.example.shop.cart.entity.CartEntity;
import com.example.shop.cart.entity.CartItemEntity;
import com.example.shop.cart.exception.CartTaskException;
import com.example.shop.cart.repository.CartItemRepository;
import com.example.shop.cart.repository.CartRepository;
import com.example.shop.product.entity.ProductEntity;
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

    // 장바구니에 상품 등록
    public void registerItem(AddCartItemDTO addCartItemDTO) {

        String mid = addCartItemDTO.getHolder();
        Long pno = addCartItemDTO.getPno();
        int quantity = addCartItemDTO.getQuantity();

        Optional<CartEntity> cartResult = cartRepository.findByHolder(mid);

        CartEntity cartEntity = cartResult.orElseGet(() -> {
            CartEntity cart = CartEntity.builder().holder(mid).build();
            return cartRepository.save(cart);
        });

        ProductEntity productEntity = productRepository.findById(pno)
                .orElseThrow(CartTaskException.Items.NOT_FOUND_PRODUCT::value);

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .cart(cartEntity)
                .product(productEntity)
                .quantity(quantity)
                .build();

        try {
            cartItemRepository.save(cartItemEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw CartTaskException.Items.CART_ITEM_REGISTER_FAIL.value();
        }
    }

    // 장바구니 아이템의 수정, 삭제
    public void modifyItem(ModifyCartItemDTO modifyCartItemDTO) {

        Long itemNo = modifyCartItemDTO.getItemNo();
        int quantity = modifyCartItemDTO.getQuantity();

        Optional<CartItemEntity> result = cartItemRepository.findById(itemNo);

        // 존재하지 않는 아이템일때
        if(result.isEmpty()) {
            throw CartTaskException.Items.NOT_FOUND_CARTITEM.value();
        }

        CartItemEntity cartItemEntity = result.get();

        if(quantity <= 0) {
            cartItemRepository.delete(cartItemEntity);
            return;
        }

        cartItemEntity.changeQuantity(quantity);
    }

    // 장바구니 아이템의 소유주 확인
    public void checkItemHolder(String holder, Long itemNo) {

        Optional<String> result = cartItemRepository.getHolderOfCartItem(itemNo);

        if(result.isEmpty()) {
            throw CartTaskException.Items.NOT_FOUND_CARTITEM.value();
        }

        if(!result.get().equals(holder)) {
            throw CartTaskException.Items.NOT_CARTITEM_OWNER.value();
        }
    }

    // 장바구니 소유주 확인 기능
    public void checkCartHolder(String holder, Long cno) {
        CartEntity cartEntity = cartRepository.findByHolder(holder)
                .orElseThrow(CartTaskException.Items.NOT_FOUND_CART::value);

        if(!cartEntity.getCno().equals(cno)) {
            throw CartTaskException.Items.NOT_FOUND_CART.value();
        }
    }
}
