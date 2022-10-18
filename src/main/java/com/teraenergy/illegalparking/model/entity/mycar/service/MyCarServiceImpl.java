package com.teraenergy.illegalparking.model.entity.mycar.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;
import com.teraenergy.illegalparking.model.entity.mycar.domain.QMyCar;
import com.teraenergy.illegalparking.model.entity.mycar.repository.MyCarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class MyCarServiceImpl implements MyCarService{

    private final JPAQueryFactory jpaQueryFactory;

    private final MyCarRepository myCarRepository;

    @Override
    public MyCar get(Integer userSeq, String carNum) {
        JPAQuery query = jpaQueryFactory.selectFrom(QMyCar.myCar);
        query.where(QMyCar.myCar.userSeq.eq(userSeq));
        query.where(QMyCar.myCar.carNum.eq(carNum));
        return (MyCar) query.fetchFirst();
    }

    @Override
    public List<MyCar> gets(Integer userSeq) {
        return null;
    }

    @Override
    public MyCar set(MyCar myCar) {
        return myCarRepository.save(myCar);
    }

    @Override
    public MyCar modify(MyCar myCar) {
        return myCarRepository.save(myCar);
    }

}
