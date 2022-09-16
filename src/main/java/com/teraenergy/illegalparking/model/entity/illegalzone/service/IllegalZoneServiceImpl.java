package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.mapper.IllegalZoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class IllegalZoneServiceImpl implements IllegalZoneService {

    private final IllegalZoneMapper illegalZoneMapper;

    @Override
    public IllegalZone get(Integer zoneSeq) {
        return illegalZoneMapper.findById(zoneSeq);
    }

    @Override
    public List<IllegalZone> getsByDong(String code) {
        return illegalZoneMapper.findByIdDong(code);
    }

    @Override
    public List<IllegalZone> getsByType(Integer typeSeq) {
        return illegalZoneMapper.findByIdType(typeSeq);
    }

    @Override
    public List<IllegalZone> getsByTypeAndDong(Integer typeSeq, String code) {
        return illegalZoneMapper.findByIdTypeAndDong(typeSeq, code);
    }

    @Override
    public List<IllegalZone> gets() {
        return illegalZoneMapper.findAll();
    }

    @Override
    public void set(IllegalZone illegalZone) {
        illegalZoneMapper.save(illegalZone);
    }

    @Override
    public void sets(List<IllegalZone> illegalZones) {
        illegalZoneMapper.saveAll(illegalZones);
    }

    @Override
    public void modify(Integer zoneSeq, Integer typeSeq, String startTime, String endTime) {
        illegalZoneMapper.modify(zoneSeq, typeSeq, startTime, endTime);
    }

    @Override
    public void delete(Integer zoneSeq) {
        illegalZoneMapper.delete(zoneSeq);
    }

}
