package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.QIllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalZoneRepository;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-21
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalZoneJpaServiceImpl implements IllegalZoneJpaService{

    private final IllegalZoneRepository illegalZoneRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<IllegalZone> gets( ) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalZone.illegalZone);
        return query.fetch();
    }

    @Override
    public IllegalZone get(Integer zoneSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalZone.illegalZone);
        query.where(QIllegalZone.illegalZone.zoneSeq.eq(zoneSeq));
        return (IllegalZone) query.fetchOne();
    }

    @Override
    public IllegalZone set(IllegalZone illegalZone) {
        return illegalZoneRepository.save(illegalZone);
    }

    @Override
    public List<IllegalZone> sets(List<IllegalZone> illegalZone) {
        return illegalZoneRepository.saveAll(illegalZone);
    }

    @Override
    public List<IllegalZone> updates(List<IllegalZone> illegalZones) {
        return illegalZoneRepository.saveAll(illegalZones);
    }

    @Override
    public List<IllegalZone> deletes(List<IllegalZone> illegalZones) {
        return illegalZoneRepository.saveAll(illegalZones);
    }
}
