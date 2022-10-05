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
public class IllegalZoneMapperServiceImpl implements IllegalZoneMapperService {

    private final IllegalZoneMapper illegalZoneMapper;

    @Override
    public IllegalZone get(Integer zoneSeq) {
        return illegalZoneMapper.findById(zoneSeq);
    }

    @Override
    public List<IllegalZone> getsByCode(List<String> codes) {
        return illegalZoneMapper.findByCode(codes);
    }

    @Override
    public List<IllegalZone> getsByGeometry(String latitude, String longitude) {
        return illegalZoneMapper.findByGeometry(latitude, longitude);
    }

    @Override
    public List<IllegalZone> getsByIllegalType(String illegalType) {
        return illegalZoneMapper.findByIllegalType(illegalType);
    }

    @Override
    public List<IllegalZone> getsByIllegalTypeAndCode(String illegalType, List<String> codes) {
        return illegalZoneMapper.findByIllegalTypeAndCode(illegalType, codes);
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
    public void modify(IllegalZone illegalZone) {
        illegalZoneMapper.modify(illegalZone);
    }

    @Override
    public void modifyByEvent(Integer zoneSeq, Integer eventSeq) {
        illegalZoneMapper.modifyByEvent(zoneSeq, eventSeq);
    }

    @Override
    public void delete(Integer zoneSeq) {
        illegalZoneMapper.delete(zoneSeq);
    }

}
