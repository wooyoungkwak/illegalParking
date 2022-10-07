package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalEvent;

import java.util.List;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalEventService {

    IllegalEvent get(Integer illegalEventSeq);

    List<IllegalEvent> gets();

    IllegalEvent set(IllegalEvent illegalEvent);

    List<IllegalEvent> sets(List<IllegalEvent> illegalEvents);
}
