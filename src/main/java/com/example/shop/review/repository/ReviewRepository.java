package com.example.shop.review.repository;

import com.example.shop.review.dto.ReviewDTO;
import com.example.shop.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

//    @Query("select r from ReviewEntity r join fetch r.productEntity where r.rno = :rno")
//    Optional<ReviewEntity> getWithProduct(@Param("rno") Long rno);

    @Query("select r from ReviewEntity r " +
    " join fetch r.productEntity rp " +
    " join fetch rp.images " +
    " where r.rno = :rno")
    Optional<ReviewEntity> getWithProduct(@Param("rno") Long rno);

    // 리뷰 목록 찾는 기능
    @Query("select r from ReviewEntity r where r.productEntity.pno = :pno")
    Page<ReviewDTO> getListByPno(@Param("pno") Long pno, Pageable pageable);
}
