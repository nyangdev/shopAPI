package com.example.shop.product.repository.search;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {

    Page<ProductListDTO> list(Pageable pageable);

    // N+1 문제 해결
    Page<ProductDTO> listWithAllImages(Pageable pageable);
}
