package com.example.shop.product.repository;

import com.example.shop.product.entity.ProductEntity;
import com.example.shop.review.entity.ReviewEntity;
import com.example.shop.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testInsert() {

        Long pno = 51L;

        ProductEntity productEntity =
                ProductEntity.builder().pno(pno).build();

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .reviewText("리뷰 내용....")
                .score(5)
                .reviewer("reviewer1")
                .productEntity(productEntity)
                .build();

        reviewRepository.save(reviewEntity);
    }

    @Transactional
    @Test
    public void testRead() {

        Long rno = 1L;

        reviewRepository.findById(rno).ifPresent(reviewEntity -> {
            System.out.println(reviewEntity);
            System.out.println(reviewEntity.getProductEntity());
        });
    }

    @Test
    public void testGetWithProduct() {
        Long rno = 1L;

        reviewRepository.getWithProduct(rno).ifPresent(reviewEntity -> {
            System.out.println(reviewEntity);
            System.out.println(reviewEntity.getProductEntity());
        });
    }

    @Transactional
    @Test
    public void testGetWithProductImage() {
        Long rno = 1L;

        reviewRepository.getWithProduct(rno).ifPresent(reviewEntity -> {
            System.out.println(reviewEntity);
            System.out.println(reviewEntity.getProductEntity());
            System.out.println(reviewEntity.getProductEntity().getImages());
        });
    }

    // 리뷰 삭제
    // 단방향 참조이기때문에 ReviewEntity 만 삭제해주면 됨
    @Transactional
    @Test
    @Commit
    public void testRemove() {
        Long rno = 1L;
        reviewRepository.deleteById(rno);
    }

    @Test
    public void testList() {
        Long pno = 51L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        reviewRepository.getListByPno(pno, pageable).getContent().forEach(reviewDTO -> {
            System.out.println(reviewDTO);
        });
    }
}
