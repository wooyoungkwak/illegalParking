package com.teraenergy.illegalparking.model.mapper.illegalzone.service;

import com.teraenergy.illegalparking.model.mapper.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.mapper.illegalzone.repository.IllegalZoneRepository;
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

    private final IllegalZoneRepository illegalZoneRepository;

    @Override
    public IllegalZone get(Integer zoneSeq) {
        return illegalZoneRepository.findById(zoneSeq);
    }

    @Override
    public List<IllegalZone> getsByDong(String code) {
        return illegalZoneRepository.findByIdDong(code);
    }

    @Override
    public List<IllegalZone> getsByType(Integer typeSeq) {
        return illegalZoneRepository.findByIdType(typeSeq);
    }

    @Override
    public List<IllegalZone> getsByTypeAndDong(Integer typeSeq, String code) {
        return illegalZoneRepository.findByIdTypeAndDong(typeSeq, code);
    }

    @Override
    public List<IllegalZone> gets() {
        return illegalZoneRepository.findAll();
    }

    @Override
    public void set(IllegalZone illegalZone) {
        illegalZoneRepository.save(illegalZone);
    }

    @Override
    public void sets(List<IllegalZone> illegalZones) {
//        illegalZoneRepository.saveAll(illegalZones);
    }

    @Override
    public void modify(Integer zoneSeq, Integer typeSeq, String startTime, String endTime) {
        illegalZoneRepository.modify(zoneSeq, typeSeq, startTime, endTime);
    }

    @Override
    public void delete(Integer zoneSeq) {
        illegalZoneRepository.delete(zoneSeq);
    }

}
