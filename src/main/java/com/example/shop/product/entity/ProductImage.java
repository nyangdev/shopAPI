package com.example.shop.product.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable // 다른 엔티티의 속성값으로 사용되기 위해서 만들어진 것이라는 표시
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProductImage implements Comparable<ProductImage> {

    private int idx;

    private String fileName;

    // idx로 정렬
    @Override
    public int compareTo(ProductImage o) {
        return this.idx - o.idx;
    }
}
