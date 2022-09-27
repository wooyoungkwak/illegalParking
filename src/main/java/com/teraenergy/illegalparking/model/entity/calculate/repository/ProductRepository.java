package com.teraenergy.illegalparking.model.entity.calculate.repository;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
