package com.popcorntalk.domain.product.repository;

import com.popcorntalk.domain.product.dto.ProductGetResponseDto;
import com.popcorntalk.domain.product.entity.QProduct;
import com.popcorntalk.global.config.QuerydslConfig;
import com.popcorntalk.global.entity.DeletionStatus;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final QuerydslConfig querydslConfig;
    QProduct qProduct = QProduct.product;

    @Override
    public Page<ProductGetResponseDto> findProducts(Pageable pageable) {
        List<ProductGetResponseDto> productResponseDtoList = querydslConfig.jpaQueryFactory()
            .select(Projections.fields(ProductGetResponseDto.class,
                qProduct.id,
                qProduct.name,
                qProduct.image,
                qProduct.description,
                qProduct.price,
                qProduct.voucherImage,
                qProduct.createdAt,
                qProduct.modifiedAt))
            .from(qProduct)
            .where(qProduct.deletionStatus.eq(DeletionStatus.valueOf("N")))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        if (Objects.isNull(productResponseDtoList)) {
            throw new IllegalArgumentException("상품이 없습니다.");
        }

        long productCount = querydslConfig.jpaQueryFactory()
            .select(qProduct)
            .from(qProduct)
            .where(qProduct.deletionStatus.eq(DeletionStatus.N))
            .fetch().size();

        return new PageImpl<>(productResponseDtoList, pageable, productCount);
    }

}
