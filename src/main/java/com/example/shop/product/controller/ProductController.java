package com.example.shop.product.controller;

import com.example.shop.product.dto.PageRequestDTO;
import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import com.example.shop.product.exception.ProductExceptions;
import com.example.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    // 목록 처리
    @GetMapping("/list")
    public ResponseEntity<Page<ProductListDTO>> list(@Validated PageRequestDTO pageRequestDTO,
                                                     Principal principal) {
        log.info(pageRequestDTO);
        // 사용자 정보 확인
        log.info(principal.getName());

        return ResponseEntity.ok(productService.getList(pageRequestDTO));
    }

    // 상품 등록
    @PostMapping("")
    public ResponseEntity<ProductDTO> register(@RequestBody @Validated ProductDTO productDTO,
                                               Principal principal) {
        log.info("register..........");
        log.info(productDTO);

        if(productDTO.getImageList() == null || productDTO.getImageList().isEmpty()) {
            throw ProductExceptions.PRODUCT_NO_IMAGE.get();
        }

        if(!principal.getName().equals(productDTO.getWriter())) {
            throw ProductExceptions.PRODUCT_WRITER_ERROR.get();
        }

        return ResponseEntity.ok(productService.register(productDTO));
    }

    // 상품 조회
    @GetMapping("/{pno}")
    public ResponseEntity<ProductDTO> read(@PathVariable("pno") Long pno) {
        log.info("read..............");
        log.info(pno);

        ProductDTO productDTO = productService.read(pno);

        return ResponseEntity.ok(productDTO);
    }

    // 상품 삭제
    // ADMIN 권한 사용자만 가능하도록 구성함
    @DeleteMapping("/{pno}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable("pno") Long pno,
                                                      Authentication authentication) {
        log.info("remove.................");
        log.info(pno);
        log.info(authentication.getName());
        log.info(authentication.getAuthorities());

        ProductDTO productDTO = productService.read(pno);

        if(!productDTO.getWriter().equals(authentication.getName())) {
            // 현재 사용자의 권한
            Collection<? extends GrantedAuthority> authorities =
                    authentication.getAuthorities();

            // ADMIN 권한이 없는 경우 예외 발생
            authorities.stream().filter(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
                    .findAny().orElseThrow(ProductExceptions.PRODUCT_WRITER_ERROR::get);
        }

        productService.remove(pno);

        return ResponseEntity.ok(Map.of("result", "success"));
    }

    // 상품 수정
    // @PathVariable로 전달된 값과 ProductDTO 안의 pno 값이 일치하는가
    // 수정하려는 상품 데이터에 이미지가 존재하는가
    // 상품의 작성자와 현재 Access Token의 사용자가 같은 사람인가
    @PutMapping("/{pno}")
    public ResponseEntity<ProductDTO> modify(@PathVariable("pno") Long pno,
                                             @RequestBody @Validated ProductDTO productDTO,
                                             Authentication authentication) {

        log.info("modify................");
        log.info(pno);
        log.info(productDTO);
        log.info(authentication.getName());

        // 1. 전달된 값과 pno 값이 일치하는가
        if(!pno.equals(productDTO.getPno())) {
            throw ProductExceptions.PRODUCT_NOT_FOUND.get();
        }

        // 2. 수정하려는 상품 데이터에 이미지가 존재하는가
        if(productDTO.getImageList() == null || productDTO.getImageList().isEmpty()) {
            throw ProductExceptions.PRODUCT_NO_IMAGE.get();
        }

        // 3. 상품의 작성자와 현재 AccessToken의 사용자가 같은 사람인가
        if(!productDTO.getWriter().equals(authentication.getName())) {
            throw ProductExceptions.PRODUCT_WRITER_ERROR.get();
        }

        return ResponseEntity.ok(productService.modify(productDTO));
    }
}
