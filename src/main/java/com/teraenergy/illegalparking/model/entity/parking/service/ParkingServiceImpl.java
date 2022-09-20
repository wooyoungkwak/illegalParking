package com.teraenergy.illegalparking.model.entity.parking.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import com.teraenergy.illegalparking.model.entity.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.runtime.Desc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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

    private final JPAQueryFactory queryFactory;

    private final ParkingRepository parkingRepository;

    @Override
    public List<Parking> gets() {
        return parkingRepository.findAll();
    }

    @Override
    public Page<Parking> gets(int pageNumber, int pageSize, ParkingFilterColumn filterColumn, String search, ParkingOrderColumn orderColumn, Sort.Direction orderBy ) {
        JPAQuery query = queryFactory.selectFrom(QParking.parking);
        int total = query.fetch().size();

        if ( search != null && search.length() > 0) {
            switch (filterColumn) {
                case parkingchrgeInfo:
                    query.where(QParking.parking.parkingchrgeInfo.like("%" + search + "%"));
                    break;
                case prkplceNm:
                    query.where(QParking.parking.prkplceNm.like("%" + search + "%"));
                    break;
            }
        }

        switch (orderColumn) {
            case parkingSeq:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QParking.parking.parkingSeq.desc());
                } else {
                    query.orderBy(QParking.parking.parkingSeq.asc());
                }
                break;
            case parkingchrgeInfo:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QParking.parking.parkingchrgeInfo.desc());
                } else {
                    query.orderBy(QParking.parking.parkingchrgeInfo.asc());
                }
                break;
            case prkplceNm:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QParking.parking.prkplceNm.desc());
                } else {
                    query.orderBy(QParking.parking.prkplceNm.asc());
                }
                break;
        }

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Parking> parkings = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Parking> page = new PageImpl<Parking>(parkings, pageRequest, total);
        return page;
    }

    @Override
    public Parking get(Integer parkingSeq) {
        return parkingRepository.findById(parkingSeq).isEmpty() == true ? null : parkingRepository.findById(parkingSeq).get();
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
