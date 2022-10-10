package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneMapperService {

    public IllegalZone get(Integer illegalZoneSeq);

    public List<IllegalZone> getsByCode(List<String> codes);

    List<IllegalZone> getsByGeometry(String latitude, String longitude);

    public List<IllegalZone> getsByIllegalType(String illegalType);

    public List<IllegalZone> getsByIllegalTypeAndCode(String illegalType, List<String> codes);

    public List<IllegalZone> gets();

    public IllegalZone set(IllegalZone illegalZone);

    public List<IllegalZone> sets(List<IllegalZone> illegalZones);

    public void modify(IllegalZone illegalZone);

    public void modifyByEvent(Integer zoneSeq, Integer eventSeq);

    public void delete(Integer zoneSeq);

}
