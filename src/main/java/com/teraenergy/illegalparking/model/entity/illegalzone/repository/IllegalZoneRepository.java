package com.teraenergy.illegalparking.model.entity.illegalzone.repository;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Date : 2022-09-21
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneRepository extends JpaRepository<IllegalZone, Integer> {
    @Query(value = "SELECT zone FROM illegal_zone zone where zone.isDel = false")
    public List<IllegalZone> findByIllegalZones();
}
