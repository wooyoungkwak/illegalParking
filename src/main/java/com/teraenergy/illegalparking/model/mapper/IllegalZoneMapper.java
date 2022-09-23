package com.teraenergy.illegalparking.model.mapper;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Mapper
public interface IllegalZoneMapper {

    IllegalZone findById(Integer zoneSeq);

    List<IllegalZone> findByIdDong(String code);

    List<IllegalZone> findByIdType(Integer illegalTypeSeq);

    List<IllegalZone> findByIdTypeAndDong(Integer illegalTypeSeq, String code);

    List<IllegalZone> findAll();

    void save(IllegalZone illegalZone);

    void saveAll(List<IllegalZone> illegalZones);

    void modify(Integer zoneSeq, Integer illegalTypeSeq, String startTime, String endTime);

    void delete(Integer zoneSeq);

}
