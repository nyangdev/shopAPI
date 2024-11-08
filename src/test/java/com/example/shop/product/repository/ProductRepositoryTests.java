package com.example.shop.product.repository;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import com.example.shop.product.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    // 여러개의 상품을 등록하는 테스트 코드
    @Test
    @Transactional
    @Commit
    public void testInsert() {

        for(int i = 1; i <= 50; i++) {
            ProductEntity productEntity = ProductEntity.builder()
                    .pname(i + "_새로운 상품")
                    .price(5000)
                    .content(i + "_상품 설명")
                    .writer("user00")
                    .build();

            productEntity.addImage(i + "_test1.jpg");
            productEntity.addImage(i + "_test2.jpg");

            productRepository.save(productEntity);

            System.out.println("New Product no: " + productEntity.getPno());
        }
    }

    // 지연로딩 확인
    @Test
    @Transactional(readOnly = true)
    public void testRead() {

        Long pno = 1L;

        Optional<ProductEntity> result = productRepository.findById(pno);

        ProductEntity productEntity = result.get();

        System.out.println(productEntity);
        System.out.println("---------------------------------");
        System.out.println(productEntity.getImages());
    }

    @Test
    @Transactional(readOnly = true)
    public void testReadQuery() {
        Long pno = 1L;

        Optional<ProductEntity> result = productRepository.getProduct(pno);

        ProductEntity productEntity = result.get();

        System.out.println(productEntity);
        System.out.println("-------------------------------");
        System.out.println(productEntity.getImages());
    }

    // 상품 수정
    // 1. 상품 가격 변경
    // 2. 새로운 상품의 이미지가 추가됨
    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        Optional<ProductEntity> result = productRepository.findById(1L);
        ProductEntity productEntity = result.get();

        productEntity.changeTitle("변경된 상품");
        productEntity.changePrice(10000);
        productEntity.addImage("new1.jpg");
        productEntity.addImage("new2.jpg");
    }

    // 상품 삭제
    @Test
    @Transactional
    @Commit
    public void testDelete() {

        productRepository.deleteById(1L);
    }

    @Test
    public void testReadDTO() {

        Long pno = 10L;

        Optional<ProductDTO> result = productRepository.getProductDTO(pno);

        ProductDTO productDTO = result.get();

        System.out.println(productDTO);
    }

    @Test
    public void testList() {
        org.springframework.data.domain.Pageable pageable
                = PageRequest.of(0, 10, Sort.by("pno").descending());

        productRepository.list(pageable);
    }

    @Test
    public void testReList() {

        org.springframework.data.domain.Pageable pageable
                = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductListDTO> result = productRepository.list(pageable);

        result.getContent().forEach(productListDTO -> {
            System.out.println(productListDTO);
        });
    }

    @Transactional
    @Test
    public void testListWithAllImages() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductDTO> result = productRepository.listWithAllImages(pageable);

        result.getContent().forEach(productDTO -> {
            System.out.println(productDTO);
        });
    }
}
