package com.example.shop.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageRequestDTO {

    @Min(1)
    @Builder.Default
    private int page = 1;

    @Min(20)
    @Max(100)
    @Builder.Default
    private int size = 20;

    // 목록의 기준인 상품 번호
    private Long pno;

    public Pageable getPageable(Sort sort) {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
    }
}
