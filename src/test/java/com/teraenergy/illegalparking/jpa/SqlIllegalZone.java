package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
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
public class SqlIllegalZone {

    @Autowired
    IllegalZoneJpaService illegalZoneJpaService;


    @Test
    public void insert() {

    }

    @Test
    public void select() {
        List<IllegalZone> illegalZones = illegalZoneJpaService.gets();
        IllegalZone illegalZone = illegalZones.get(0);
//        try {
//            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println(illegalZone.getPolygon());
//
//        for ( IllegalZone illegalZone : illegalZones) {
//            System.out.println(illegalZone.getCode());
//            System.out.println(illegalZone.getName());
//            System.out.println(illegalZone.getPolygon());
//            System.out.println(illegalZone.getIllegalType().getTypeSeq());
//            System.out.println(illegalZone.getIllegalType().getName());
//        }

    }
}
