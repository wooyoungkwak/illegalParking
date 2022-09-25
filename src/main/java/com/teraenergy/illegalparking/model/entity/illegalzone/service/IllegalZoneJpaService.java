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

    IllegalZone modify(IllegalZone illegalZone);

    List<IllegalZone> modifies(List<IllegalZone> illegalZones);

    long remove(Integer illegalZoneSeq);

    long removes(List<Integer> illegalZoneSeqs);
}
