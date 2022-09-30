package com.teraenergy.illegalparking.model.dto.illegalzone.domain;

import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class IllegalZoneDto {
    Integer zoneSeq;
    String name;
    String polygon;
    String code;
    Integer eventSeq;  // null

    String illegalType;
    Boolean usedFirst;
    String firstStartTime;
    String firstEndTime;
    Boolean usedSecond;
    String secondStartTime;
    String secondEndTime;
    String zoneGroupType;
}
