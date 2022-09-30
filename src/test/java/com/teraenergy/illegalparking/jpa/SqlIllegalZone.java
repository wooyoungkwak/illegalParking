package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalZoneRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Date : 2022-09-21
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlIllegalZone {

    @Autowired
    IllegalZoneJpaService illegalZoneJpaService;

    @Autowired
    IllegalZoneRepository repository;

    @Autowired
    IllegalEventService illegalEventService;

    @Test
    public void insertByEvent() {

        IllegalEvent illegalEvent = new IllegalEvent();
        illegalEvent.setUsedFirst(true);
        illegalEvent.setUsedSecond(true);
        illegalEvent.setZoneGroupType(ZoneGroupType.GROUP_A);

        illegalEventService.set(illegalEvent);
    }

    @Test
    public void select() {


    }

}
