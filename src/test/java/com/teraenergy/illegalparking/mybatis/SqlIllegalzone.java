package com.teraenergy.illegalparking.mybatis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
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
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlIllegalzone {

    @Autowired
    IllegalZoneMapperService illegalZoneMapperService;

    @Autowired
    IllegalZoneService illegalZoneService;

    @Autowired
    IllegalEventService illegalEventService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void insert() {
        insertOne();
        insertList();
    }

    @Test
    public void insertOne(){
        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setCode("5013032000");
        illegalZone.setIsDel(false);
        illegalZoneMapperService.set(illegalZone);
    }

    @Test
    public void insertList() {
        List<IllegalZone> illegalZones = Lists.newArrayList();

        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setCode("1100000000");
        illegalZone.setIsDel(false);

        IllegalZone illegalZone2 = new IllegalZone();
        illegalZone2.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone2.setCode("5013032026");
        illegalZone2.setIsDel(false);

        illegalZones.add(illegalZone);
        illegalZones.add(illegalZone2);

        illegalZoneMapperService.sets(illegalZones);
    }

//    @Test
//    public void update() {
//        IllegalEvent illegalEvent = illegalEventService.get(1);
//        IllegalZone illegalZone = illegalZoneMapperService.get(1);
//
//        illegalZone.setCode("1111100000");
//        illegalZoneMapperService.modify(illegalZone);
//
//        illegalZone.setIllegalEvent(illegalEvent);
//        illegalZoneMapperService.modifyByEvent(illegalZone.getZoneSeq(), illegalEvent.getEventSeq());
//    }

    @Test
    public void delete() {
        illegalZoneMapperService.delete(1);
    }


    @Test
    void select() {
        try {
            List<IllegalZone> illegalZones = illegalZoneService.gets();
            System.out.println(objectMapper.writeValueAsString(illegalZones));

//            List<String> codes = Lists.newArrayList();
//            codes.add("1111100000");
//            codes.add("5013032026");
//            List<IllegalZone> illegalZones = illegalZoneMapperService.getsByCode(codes);
//            System.out.println(objectMapper.writeValueAsString(illegalZones));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        if( !illegalZones.isEmpty()) {
//            IllegalZone illegalZone = illegalZones.get(0);
//
//            GeometryFactory geometryFactory = new GeometryFactory();
//            Point polyInnerPoint = geometryFactory.createPoint(new Coordinate(126.56866907996265,33.451180712229686));
//            Point polyInnerPoint2 = geometryFactory.createPoint(new Coordinate(126.56915449563454,33.450893887400085));
//            Point polyInnerPoint3 = geometryFactory.createPoint(new Coordinate(126.56826037812841,33.45117928982703));
//            Point polyInnerPoint4 = geometryFactory.createPoint(new Coordinate(126.56816407185906,33.451079778302194));
//            Point polyOuterPoint = geometryFactory.createPoint(new Coordinate(126.56916190732109,33.45157011423124));
//            Point polyOuterPoint2 = geometryFactory.createPoint(new Coordinate(126.56652326588099,33.45011835549815));
//            Point polyOuterPoint3 = geometryFactory.createPoint(new Coordinate(126.56601552728885,33.450567380165516));
//
//            System.out.println("seq = " + illegalZone.getZoneSeq());
//            System.out.println(illegalZone.getPolygon());
//
//            Polygon polygon;
//            try {
//                polygon = (Polygon) new WKTReader().read(String.format(illegalZone.getPolygon()));
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//
//            boolean innerWithin = polyInnerPoint.within     (polygon);
//            boolean innerWithin2 = polyInnerPoint2.within   (polygon);
//            boolean innerWithin3 = polyInnerPoint3.within   (polygon);
//            boolean innerWithin4 = polyInnerPoint4.within   (polygon);
//            boolean outerWithin = polyOuterPoint.within     (polygon);
//            boolean outerWithin2 = polyOuterPoint2.within   (polygon);
//            boolean outerWithin3 = polyOuterPoint3.within   (polygon);
//
//            System.out.println("innerWithin :" + innerWithin);
//            System.out.println("innerWithin2 :" + innerWithin2);
//            System.out.println("innerWithin3 :" + innerWithin3);
//            System.out.println("innerWithin4 :" + innerWithin4);
//            System.out.println("outerWithin :" + outerWithin);
//            System.out.println("outerWithin2 :" + outerWithin2);
//            System.out.println("outerWithin3 :" + outerWithin3);
//        }

    }
}
