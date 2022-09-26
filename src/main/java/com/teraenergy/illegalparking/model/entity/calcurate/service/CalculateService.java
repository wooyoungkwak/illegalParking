package com.teraenergy.illegalparking.model.entity.calcurate.service;

import com.teraenergy.illegalparking.model.entity.calcurate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CalculateService {
    public Calculate get(Integer calculateSeq);

    public List<Calculate> gets();

    public Page<Calculate> gets(int pageNumber, int pageSize, CalculateFilterColumn filterColumn, String search, CalculateOrderColumn orderColumn, Sort.Direction orderBy);

    public Calculate save(Calculate calculate);

    public List<Calculate> saves(List<Calculate> calculates);

    public Calculate modify(Calculate calculate);

    public Calculate remove(Integer calculateSeq);


}
