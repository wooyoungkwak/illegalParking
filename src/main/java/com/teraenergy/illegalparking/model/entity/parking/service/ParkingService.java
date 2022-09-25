package com.teraenergy.illegalparking.model.entity.parking.service;

import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Service
public interface ParkingService {

    public List<Parking> gets();

    public Page<Parking> gets(int pageNumber, int pageSize, ParkingFilterColumn filterColumn, String search, ParkingOrderColumn orderColumn, Sort.Direction orderBy);

    public Parking get(Integer prkingSeq);

    public List<Parking> sets(List<Parking> parkings);

    public Parking set(Parking parking);

    public Parking modify(Parking parking);

    public long remove(Parking parking);

}
