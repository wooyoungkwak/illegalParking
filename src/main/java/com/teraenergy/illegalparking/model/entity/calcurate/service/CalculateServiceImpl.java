package com.teraenergy.illegalparking.model.entity.calcurate.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.calcurate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calcurate.domain.QCalculate;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.calcurate.repository.CalculateRepository;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class CalculateServiceImpl implements CalculateService{

    private final JPAQueryFactory jpaQueryFactory;

    private final CalculateRepository calculateRepository;

    @Override
    public Calculate get(Integer calculateSeq) {
        Optional<Calculate> optional = calculateRepository.findById(calculateSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Calculate> gets() {
        return calculateRepository.findAll();
    }

    @Override
    public Page<Calculate> gets(int pageNumber, int pageSize, CalculateFilterColumn filterColumn, String search, CalculateOrderColumn orderColumn, Sort.Direction orderBy) {
        JPAQuery query = jpaQueryFactory.selectFrom(QCalculate.calculate);

        if ( search != null && search.length() > 0) {
            switch (filterColumn) {
                case user:
                    query.where(QCalculate.calculate.user.name.contains(search));
                    break;
                case product:
                    query.where(QCalculate.calculate.point.product.name.contains(search));
                    break;
            }
        }

        query.where(QCalculate.calculate.isDel.isFalse());

        int total = query.fetch().size();

        switch (orderColumn) {
            case calculateSeq:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QCalculate.calculate.calculateSeq.desc());
                } else {
                    query.orderBy(QCalculate.calculate.calculateSeq.asc());
                }
                break;
            case user:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QCalculate.calculate.user.name.desc());
                } else {
                    query.orderBy(QCalculate.calculate.user.name.asc());
                }
                break;
            case product:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QCalculate.calculate.point.product.name.desc());
                } else {
                    query.orderBy(QCalculate.calculate.point.product.name.asc());
                }
                break;
            case currentPoint:
                if ( orderBy.equals(Sort.Direction.DESC)) {
                    query.orderBy(QCalculate.calculate.currentPoint.desc());
                } else {
                    query.orderBy(QCalculate.calculate.currentPoint.asc());
                }
                break;
        }

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Calculate> calculates = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Calculate> page = new PageImpl<Calculate>(calculates, pageRequest, total);
        return page;
    }

    @Override
    public Calculate save(Calculate calculate) {
        return calculateRepository.save(calculate);
    }

    @Override
    public List<Calculate> saves(List<Calculate> calculates) {
        return calculateRepository.saveAll(calculates);
    }

    @Override
    public Calculate modify(Calculate calculate) {
        return calculateRepository.save(calculate);
    }

    @Override
    public Calculate remove(Integer calculateSeq) {

        return null;
    }
}
