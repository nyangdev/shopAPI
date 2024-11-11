package com.example.shop.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductListDTO {

    // 상품 목록 보여주는 DTO
    
    private Long pno;
    private String pname;
    private int price;
    private String writer;
    
    // 상품의 이미지는 하나만 담을 수 있게 구성
    private String productImage;

    // 리뷰 갯수
    private long reviewCount;
    
}