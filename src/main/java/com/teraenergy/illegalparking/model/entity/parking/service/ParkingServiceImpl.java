package com.teraenergy.illegalparking.model.entity.parking.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import com.teraenergy.illegalparking.model.entity.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.runtime.Desc;
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

    private final EntityManager entityManager;

    private final ParkingRepository parkingRepository;

    @Override
    public List<Parking> gets() {
        return parkingRepository.findAll();
    }

    @Override
    public List<Parking> gets(int offset, int limit, ParkingOrderColumn orderColumn, Sort.Direction orderBy ) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        JPAQuery  query = queryFactory.selectFrom(QParking.parking)
                .limit(limit)
                .offset(offset);
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

        return query.fetch();
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
