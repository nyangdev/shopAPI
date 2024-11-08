package com.example.shop.product.repository;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.entity.ProductEntity;
import com.example.shop.product.repository.search.ProductSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductSearch {

    // fetch 조인을 어노테이션으로 사용가능하게 만든 기능
//    @EntityGraph(attributePaths = {"images"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Query("select p from ProductEntity p where p.pno = :pno")
//    Optional<ProductEntity> getProduct(@Param("pno") Long pno);

    // fetch 조인 사용
    @Query("select p from ProductEntity p join fetch p.images pi where p.pno = :pno")
    Optional<ProductEntity> getProduct(@Param("pno") Long pno);

    // ProductDTO의 생성자를 이용해서 DTO로 자동 처리
    @Query("select p from ProductEntity p join fetch p.images pi where p.pno = :pno")
    Optional<ProductDTO> getProductDTO(@Param("pno") Long pno);
}
