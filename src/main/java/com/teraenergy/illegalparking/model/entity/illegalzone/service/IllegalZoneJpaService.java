package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Date : 2022-09-21
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalZoneJpaService {

    List<IllegalZone> gets();

    IllegalZone get(Integer illegalZoneSeq);

    IllegalZone set(IllegalZone illegalZone);

    List<IllegalZone> sets(List<IllegalZone> illegalZone);

    List<IllegalZone> updates(List<IllegalZone> illegalZones);

    List<IllegalZone> deletes(List<IllegalZone> illegalZones);
}