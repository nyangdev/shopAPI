package com.example.shop.product.service;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.entity.ProductEntity;
import com.example.shop.product.exception.ProductExceptions;
import com.example.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    public ProductDTO register(ProductDTO productDTO) {

        try {
            log.info("register.............");
            log.info(productDTO);

            ProductEntity productEntity = productDTO.toEntity();

            productRepository.save(productEntity);

            // 등록된 엔티티를 기반으로 DTO 객체 반환
            return new ProductDTO(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_REGISTRED.get();
        }
    }

    // 상품 조회
    @Transactional(readOnly = true)
    public ProductDTO read(Long pno) {
        log.info("read...........");
        log.info(pno);

        java.util.Optional<ProductEntity> result = productRepository.getProduct(pno);

        ProductEntity productEntity
                = result.orElseThrow(() -> ProductExceptions.PRODUCT_NOT_FOUND.get());

        return new ProductDTO(productEntity);
    }

    // 상품 삭제
    public void remove(Long pno) {
        log.info("remove...............");
        log.info(pno);

        java.util.Optional<ProductEntity> result = productRepository.getProduct(pno);

        ProductEntity productEntity
                = result.orElseThrow(() -> ProductExceptions.PRODUCT_NOT_FOUND.get());

        try {
            productRepository.delete(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_REMOVED.get();
        }
    }

    // 상품 수정
    public ProductDTO modify(ProductDTO productDTO) {
        log.info("modify...............");
        log.info(productDTO);

        java.util.Optional<ProductEntity> result = productRepository.findById(productDTO.getPno());

        ProductEntity productEntity
                = result.orElseThrow(() -> ProductExceptions.PRODUCT_NOT_FOUND.get());

        try {
            // 상품 정보 수정
            productEntity.changePrice(productDTO.getPrice());
            productEntity.changeTitle(productDTO.getPname());

            // 기존 이미지들 삭제
            productEntity.clearImages();

            // 새로운 이미지들 추가
            java.util.List<String> fileNames = productDTO.getImageList();

            if(fileNames != null && !fileNames.isEmpty()) {
                fileNames.forEach(productEntity::addImage);
            }

            productRepository.save(productEntity);

            return new ProductDTO(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_MODIFIED.get();
        }
    }
}
