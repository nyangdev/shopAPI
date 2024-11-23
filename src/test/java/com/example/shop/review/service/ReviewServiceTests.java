package com.example.shop.review.service;

import com.example.shop.review.dto.ReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewServiceTests {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void testRegister() {
        // 예외 처리 확인
        // 존재하지않는 상품 번호 입력
        Long pno = 101L;

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewText("리뷰 내용")
                .score(5)
                .reviewer("reviewer1")
                .pno(pno)
                .build();

        reviewService.register(reviewDTO);
    }

    @Test
    public void testRegister1() {
        Long pno = 100L;

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewText("리뷰 내용")
                .score(5)
                .reviewer("reviewer1")
                .pno(pno)
                .build();

        reviewService.register(reviewDTO);
    }

    // 리뷰 조회
    @Test
    public void testRead() {
        Long rno = 2L;

        ReviewDTO reviewDTO = reviewService.read(rno);

        System.out.println(reviewDTO);
    }
}