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

    public List<IllegalZone> gets(Integer lawDongSeq);

    public List<IllegalZone> gets();

    public void set(IllegalZone illegalZone);

    public void sets(List<IllegalZone> illegalZones);
}
