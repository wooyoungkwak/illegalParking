package com.teraenergy.illegalparking.model.entity.parking.service;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
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
    public List<Parking> gets(int offset, int limit, Object orderBy ) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query.selectFrom(QParking.parking)
                .limit(limit)
                .offset(offset)
                .orderBy((OrderSpecifier<?>) orderBy)
                .fetch();
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
