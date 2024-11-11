package com.example.shop.product.repository.search;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {

    Page<ProductListDTO> list(Pageable pageable);

    // N+1 문제 해결
    Page<ProductDTO> listWithAllImages(Pageable pageable);

    // 리뷰의 갯수를 가져오면서 페이징처리까지
    Page<ProductListDTO> listWithReviewCount(Pageable pageable);

    // 상품의 모든 이미지와 리뷰 수
    Page<ProductDTO> listWithAllImagesReviewCount(Pageable pageable);
}
