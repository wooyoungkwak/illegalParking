package com.teraenergy.illegalparking.model.entity.illegalzone.repository;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalEventRepository extends JpaRepository<IllegalEvent, Integer> {
}
