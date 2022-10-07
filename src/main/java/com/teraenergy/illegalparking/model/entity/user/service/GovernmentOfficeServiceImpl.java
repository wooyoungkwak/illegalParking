package com.teraenergy.illegalparking.model.entity.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.user.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.user.repository.GovernmentOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class GovernmentOfficeServiceImpl implements GovernmentOfficeService{

    private final GovernmentOfficeRepository governmentOfficeRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GovernmentOffice> gets() {
//        jpaQueryFactory.selectFrom()
        return null;
    }

    @Override
    public GovernmentOffice set(GovernmentOffice governmentOffice) {
        return governmentOfficeRepository.save(governmentOffice);
    }
}
