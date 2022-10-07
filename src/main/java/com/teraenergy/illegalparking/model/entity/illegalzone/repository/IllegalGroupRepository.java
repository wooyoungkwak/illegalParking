package com.teraenergy.illegalparking.model.entity.illegalzone.repository;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalGroupRepository extends JpaRepository<IllegalGroup, Integer> {
}
