package com.teraenergy.illegalparking.model.entity.illegalType.repository;

import com.teraenergy.illegalparking.model.entity.illegalType.domain.IllegalType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-16
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
public interface IllegalTypeRepository extends JpaRepository<IllegalType, Integer> {
}
