package com.teraenergy.illegalparking.model.entity.governmentoffice.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.governmentoffice.repository.GovernmentOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public GovernmentOffice get(Integer officeSeq) {
        Optional<GovernmentOffice> optional = governmentOfficeRepository.findById(officeSeq);
        if ( optional.isEmpty() ) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<GovernmentOffice> gets() {
        return governmentOfficeRepository.findAll();
    }

    @Override
    public GovernmentOffice set(GovernmentOffice governmentOffice) {
        return governmentOfficeRepository.save(governmentOffice);
    }
}
