package com.teraenergy.illegalparking.model.entity.parking.repository;

import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ParkingRepository extends JpaRepository<Parking, Integer> {
}
