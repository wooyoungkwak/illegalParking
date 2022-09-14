package com.teraenergy.illegalparking.model.entity.lawdong.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.QLawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.repository.LawDongRepository;
import lombok.RequiredArgsConstructor;
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
public class LawDongServiceImpl implements LawDongService{

    private final EntityManager entityManager;

    private final LawDongRepository lawDongRepository;

    @Override
    public LawDong get(Integer lawDongSeq) {
        return lawDongRepository.findById(lawDongSeq).get();
    }

    @Override
    public LawDong getFromLnmadr(String lnmadr) {
        String temp[] = lnmadr.split("동");
        lnmadr = (temp[0] + "동").trim();
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query.selectFrom(QLawDong.lawDong)
                .where(QLawDong.lawDong.isDel.isFalse())
                .where(QLawDong.lawDong.name.contains(lnmadr))
                .fetchOne();
    }

    @Override
    public List<LawDong> gets() {
        return lawDongRepository.findAll();
    }

    @Override
    public void set(LawDong lawDong) {
        lawDongRepository.save(lawDong);
    }

    @Override
    public void sets(List<LawDong> lawDongs) {
        lawDongRepository.saveAll(lawDongs);
    }

    @Override
    public void delete(LawDong lawDong) {
        lawDong.setIsDel(true);
        lawDongRepository.save(lawDong);
    }
}
