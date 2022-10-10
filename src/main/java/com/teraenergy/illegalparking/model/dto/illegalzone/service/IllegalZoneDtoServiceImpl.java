package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalZoneDto;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Service
public class IllegalZoneDtoServiceImpl implements IllegalZoneDtoService {

    @Override
    public IllegalZoneDto getToIllegalZoneDto(IllegalZone illegalZone) {
        IllegalZoneDto illegalZoneDto = new IllegalZoneDto();
        illegalZoneDto.setZoneSeq(illegalZone.getZoneSeq());
        illegalZoneDto.setPolygon(illegalZone.getPolygon());
        illegalZoneDto.setCode(illegalZone.getCode());

        if (illegalZone.getIllegalEvent() != null) {
            illegalZoneDto.setEventSeq(illegalZone.getIllegalEvent().getEventSeq());
            illegalZoneDto.setName(illegalZone.getIllegalEvent().getName());
            illegalZoneDto.setIllegalType(illegalZone.getIllegalEvent().getIllegalType().toString());
            illegalZoneDto.setUsedFirst(illegalZone.getIllegalEvent().getUsedFirst());
            illegalZoneDto.setFirstStartTime(illegalZone.getIllegalEvent().getFirstStartTime());
            illegalZoneDto.setFirstEndTime(illegalZone.getIllegalEvent().getFirstEndTime());
            illegalZoneDto.setUsedSecond(illegalZone.getIllegalEvent().getUsedSecond());
            illegalZoneDto.setSecondStartTime(illegalZone.getIllegalEvent().getSecondStartTime());
            illegalZoneDto.setSecondEndTime(illegalZone.getIllegalEvent().getSecondEndTime());
        }
        return illegalZoneDto;
    }

    @Override
    public IllegalZone getToIllegalZone(IllegalZoneDto illegalZoneDto) {

        IllegalZone illegalZone = new IllegalZone();
        if ( illegalZoneDto.getZoneSeq() != null) {
            illegalZone.setZoneSeq(illegalZoneDto.getZoneSeq());
        }
        illegalZone.setPolygon(illegalZoneDto.getPolygon());
        illegalZone.setCode(illegalZone.getCode());
        illegalZone.setIsDel(false);

        if (illegalZoneDto.getEventSeq() != null) {
            IllegalEvent illegalEvent = new IllegalEvent();
            illegalEvent.setEventSeq(illegalZoneDto.getEventSeq());
            illegalEvent.setName(illegalZoneDto.getName());
            illegalEvent.setIllegalType(IllegalType.valueOf(illegalZoneDto.getIllegalType()));
            illegalEvent.setUsedFirst(illegalZoneDto.getUsedFirst());
            illegalEvent.setFirstStartTime(illegalZoneDto.getFirstStartTime());
            illegalEvent.setFirstEndTime(illegalZoneDto.getFirstEndTime());
            illegalEvent.setUsedSecond(illegalZoneDto.getUsedSecond());
            illegalEvent.setSecondStartTime(illegalZoneDto.getSecondStartTime());
            illegalEvent.setSecondEndTime(illegalZoneDto.getSecondEndTime());
            illegalZone.setIllegalEvent(illegalEvent);
        } else {
            illegalZone.setIllegalEvent(null);
        }

        return illegalZone;
    }

    @Override
    public List<IllegalZone> getsToIllegalZones(List<IllegalZoneDto> illegalZoneDtos) {
        List<IllegalZone> illegalZones = Lists.newArrayList();
        for(IllegalZoneDto illegalZoneDto : illegalZoneDtos) {
            illegalZones.add(getToIllegalZone(illegalZoneDto));
        }
        return illegalZones;
    }

}
