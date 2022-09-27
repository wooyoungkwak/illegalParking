package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalZoneRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
public class SqlIllegalZone {

    @Autowired
    IllegalZoneJpaService illegalZoneJpaService;

    @Autowired
    IllegalZoneRepository repository;


    @Test
    public void insert() {

    }

    @Test
    public void select() {


    }

}
