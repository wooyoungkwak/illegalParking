package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.domain.QProduct;
import com.teraenergy.illegalparking.model.entity.calculate.enums.Brand;
import com.teraenergy.illegalparking.model.entity.calculate.enums.ProductFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.ProductOrderColumn;
import com.teraenergy.illegalparking.model.entity.calculate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final JPAQueryFactory jpaQueryFactory;

    private final ProductRepository productRepository;

    @Override
    public Product get(Integer productSeq) {
        Optional<Product> optional = productRepository.findById(productSeq);
        if ( optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Product> gets() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> gets(List<Integer> productSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QProduct.product).where(QProduct.product.productSeq.in(productSeqs));
        return query.fetch();
    }

    @Override
    public Page<Product> gets(int pageNumber, int pageSize, ProductFilterColumn filterColumn, String search, ProductOrderColumn orderColumn, Sort.Direction orderBy) {
        JPAQuery query = jpaQueryFactory.selectFrom(QProduct.product);

        if ( search != null && search.length() > 0) {
            switch (filterColumn) {
                case name:
                    query.where(QProduct.product.name.contains(search));
                    break;
                case brand:
                    List<Brand> brands = Lists.newArrayList();
                    for(Brand brand : Brand.values()) {
                        if ( brand.getValue().indexOf(search) > -1 ) {
                            brands.add(brand);
                        }
                    }
                    query.where(QProduct.product.brand.in(brands));
                case point:
                    query.where(QProduct.product.pointValue.eq(Long.parseLong(search)));
                    break;
            }
        }

        query.where(QProduct.product.isDel.isFalse());

        int total = query.fetch().size();

        switch (orderColumn) {
            case productSeq:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QProduct.product.productSeq.desc());
                } else {
                    query.orderBy(QProduct.product.productSeq.asc());
                }
                break;
            case name:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QProduct.product.name.desc());
                } else {
                    query.orderBy(QProduct.product.name.asc());
                }
                break;
            case point:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QProduct.product.pointValue.desc());
                } else {
                    query.orderBy(QProduct.product.pointValue.asc());
                }
                break;
            case regDt:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QProduct.product.regDt.desc());
                } else {
                    query.orderBy(QProduct.product.regDt.asc());
                }
                break;
        }

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Product> products = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = new PageImpl<Product>(products, pageRequest, total);
        return page;
    }

    @Override
    public Product set(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> sets(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public long remove(Integer integer) {
        JPAUpdateClause query = jpaQueryFactory.update(QProduct.product).set(QProduct.product.isDel, true);
        return query.execute();
    }
}
