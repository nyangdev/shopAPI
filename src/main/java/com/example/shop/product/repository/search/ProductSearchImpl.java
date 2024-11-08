package com.example.shop.product.repository.search;

import com.example.shop.product.dto.ProductDTO;
import com.example.shop.product.dto.ProductListDTO;
import com.example.shop.product.entity.ProductEntity;
import com.example.shop.product.entity.QProductEntity;
import com.example.shop.product.entity.QProductImage;
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
}
