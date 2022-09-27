package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.enums.ProductFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.ProductOrderColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ProductService {

    Product get(Integer productSeq);

    List<Product> gets();

    List<Product> gets(List<Integer> productSeqs);

    Page<Product> gets(int pageNumber, int pageSize, ProductFilterColumn filterColumn, String search, ProductOrderColumn orderColumn, Sort.Direction orderBy);

    Product set(Product product);

    List<Product> sets(List<Product> products);

    long remove(Integer integer);

}
