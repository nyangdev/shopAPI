package com.example.shop.product.repository.search;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import com.example.shop.product.entity.ProductEntity;
import com.example.shop.product.entity.QProductEntity;
import com.example.shop.product.entity.QProductImage;
import com.example.shop.review.entity.QReviewEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage);

        // where productImage.idx = 0
        query.where(productImage.idx.eq(0));

        // Long pno, String pname, int price, String writer, String productImage
        // DB의 필요한 속성만을 조회하는 것을 projection 이라고 함
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections.bean(ProductListDTO.class,
                productEntity.pno,
                productEntity.pname,
                productEntity.price,
                productEntity.writer,
                productImage.fileName.as("productImage")));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        java.util.List<ProductListDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImages(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;

        JPQLQuery<ProductEntity> query = from(productEntity);

        this.getQuerydsl().applyPagination(pageable, query);

        List<ProductEntity> entityList = query.fetch();

        long count = query.fetchCount();

//        for(ProductEntity entity : entityList) {
//            System.out.println(entity);
//            System.out.println(entity.getImages());
//            System.out.println("----------------------------------");
//        }

        List<ProductDTO> dtoList = entityList.stream()
                .map(ProductDTO::new).toList();

        return new PageImpl<>(dtoList, pageable, count);
    }

    // 상품의 대표이미지와 리뷰 수
    @Override
    public Page<ProductListDTO> listWithReviewCount(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;
        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QProductImage productImage = QProductImage.productImage;

        // 리뷰가 존재하지 않을수도 있기때문에 left join
        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(reviewEntity).on(reviewEntity.productEntity.eq(productEntity));
        query.leftJoin(productEntity.images, productImage);

        query.where(productImage.idx.eq(0)); // 하나의 상품에는 하나의 상품 이미지만

        // query 를 기준으로 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // group by
        // 여러 개의 리뷰가 있을 수 있으므로 상품별로 group by 처리
        query.groupBy(productEntity);

        // Long pno, String pname, int price, String writer, String productImage
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections
                .bean(ProductListDTO.class,
                        productEntity.pno,
                        productEntity.pname,
                        productEntity.price,
                        productEntity.writer,
                        productImage.fileName.as("productImage"),
                        // 리뷰 숫자가 중복되지 않도록 설정
                        reviewEntity.countDistinct().as("reviewCount")));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        java.util.List<ProductListDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    // 상품의 모든 이미지와 리뷰 수
    @Override
    public Page<ProductDTO> listWithAllImagesReviewCount(Pageable pageable) {
        
        QProductEntity productEntity = QProductEntity.productEntity;
        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        
        // 쿼리 start
        JPQLQuery<ProductEntity> query = from(productEntity);
        // 리뷰가 없는 경우를 생각해서 left join
        query.leftJoin(reviewEntity).on(reviewEntity.productEntity.eq(productEntity));

        // query paging 처리
        this.getQuerydsl().applyPagination(pageable, query);

        // 상품별로 group by 처리
        query.groupBy(productEntity);

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(productEntity, reviewEntity.countDistinct());

        List<Tuple> result = tupleJPQLQuery.fetch();

        List<ProductDTO> dtoList = result.stream().map(tuple -> {
            ProductEntity product = tuple.get(0, ProductEntity.class);
            long count = tuple.get(1, Long.class);

            ProductDTO dto = new ProductDTO(product);

            dto.setReviewCount(count);

            return dto;
        }).toList();

        return new PageImpl<>(dtoList, pageable, tupleJPQLQuery.fetchCount());
    }
}
