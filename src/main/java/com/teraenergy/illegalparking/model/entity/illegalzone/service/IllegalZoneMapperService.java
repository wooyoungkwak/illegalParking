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

    public List<IllegalZone> getsByIllegalType(Integer illegalType);

    public List<IllegalZone> getsByIllegalTypeAndCode(Integer illegalTypeSeq, List<String> codes);

    public List<IllegalZone> gets();

    public void set(IllegalZone illegalZone);

    public void sets(List<IllegalZone> illegalZones);

    public void modify(IllegalZone illegalZone);

    public void modifyByEvent(Integer zoneSeq, Integer eventSeq);

    public void delete(Integer zoneSeq);

}
