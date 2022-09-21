package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    @Override
    public List<IllegalZone> gets( ) {
//        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalZone.illegalZone);
//        return query.fetch();

        String queryStr = "SELECT zone.zoneSeq, zone.name, zone.polygon, zone.isDel, zone.code, zone.StartTime, zone.EndTime, zone.illegalType FROM illegal_zone zone where zone.isDel = false";
//        TypedQuery<IllegalZone> query = entityManager.createQuery(queryStr, IllegalZone.class).getResultList();
//        return query.getResultList();
        return entityManager.createQuery(queryStr, IllegalZone.class).getResultList();
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
