package com.teraenergy.illegalparking.model.mapper.illegalzone.service;

import com.teraenergy.illegalparking.model.mapper.illegalzone.domain.IllegalZone;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneService {

    public IllegalZone get(Integer illegalZoneSeq);

    public List<IllegalZone> getsByDong(Double code);

    public List<IllegalZone> getsByType(Integer typeSeq);

    public List<IllegalZone> getsByTypeAndDong(Integer typeSeq, Double code);

    public List<IllegalZone> gets();

    public void set(IllegalZone illegalZone);

    public void sets(List<IllegalZone> illegalZones);

    public void modify(Integer zoneSeq, Integer typeSeq, String startTime, String endTime);

    public void delete(Integer zoneSeq);

}
