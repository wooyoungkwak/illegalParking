package com.teraenergy.illegalparking.model.entity.parking.service;

import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ParkingServiceImpl implements ParkingService{

    final private ParkingRepository parkingRepository;

    @Override
    public List<Parking> gets() {
        return parkingRepository.findAll();
    }

    @Override
    public Parking get(Integer prkingSeq) {
        return parkingRepository.findById(prkingSeq).isEmpty() == true ? null : parkingRepository.findById(prkingSeq).get();
    }

    @Override
    public List<Parking> sets(List<Parking> parkings) {
        return parkingRepository.saveAll(parkings);
    }

    @Override
    public Parking set(Parking parking) {
        return parkingRepository.save(parking);
    }

    @Override
    public Parking delete(Parking parking) {
        return parkingRepository.save(parking);
    }
}
