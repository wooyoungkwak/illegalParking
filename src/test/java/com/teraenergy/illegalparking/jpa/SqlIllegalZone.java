package com.teraenergy.illegalparking.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalZoneRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    IllegalZoneService illegalZoneService;

    @Autowired
    IllegalZoneRepository repository;

    @Autowired
    IllegalEventService illegalEventService;

    @Autowired
    IllegalGroupServcie illegalGroupServcie;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void insertByEvent() {

        IllegalEvent illegalEvent = new IllegalEvent();
        illegalEvent.setUsedFirst(false);
        illegalEvent.setUsedSecond(false);
        illegalEvent.setFirstStartTime("12:00");
        illegalEvent.setFirstEndTime("13:00");
        illegalEvent.setSecondStartTime("18:00");
        illegalEvent.setSecondEndTime("20:00");
        illegalEvent.setZoneGroupType(ZoneGroupType.GROUP_A);
        illegalEvent.setIllegalType(IllegalType.ILLEGAL);
        illegalEvent.setName("샘플1");

        IllegalEvent illegalEvent2 = new IllegalEvent();
        illegalEvent2.setUsedFirst(false);
        illegalEvent2.setUsedSecond(false);
        illegalEvent2.setFirstStartTime("12:30");
        illegalEvent2.setFirstEndTime("13:30");
        illegalEvent2.setSecondStartTime("18:30");
        illegalEvent2.setSecondEndTime("20:30");
        illegalEvent2.setZoneGroupType(ZoneGroupType.GROUP_B);
        illegalEvent2.setIllegalType(IllegalType.FIVE_MINUTE);
        illegalEvent2.setName("샘플2");

        List<IllegalEvent> illegalEvents = Lists.newArrayList();
        illegalEvents.add(illegalEvent);
        illegalEvents.add(illegalEvent2);

        System.out.println("illegalEvents size = " + illegalEvents.size());
        List<IllegalEvent> illegalEventList = illegalEventService.sets(illegalEvents);
        System.out.println("illegalEventList size = " + illegalEventList.size());
    }

    @Test
    public void insertByIllegalGroup(){
        IllegalGroup illegalGroup = new IllegalGroup();
        illegalGroup.setLocationType(LocationType.JEONNAM);
        illegalGroup.setName("나주A");
        illegalGroupServcie.set(illegalGroup);
    }

    @Test
    public void select() {
        IllegalEvent illegalEvent = illegalEventService.get(1);
        try {
            System.out.println(objectMapper.writeValueAsString(illegalEvent));
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

}
