package com.teraenergy.illegalparking.mybatis;


import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
public class SqlIllegalzone {

    @Autowired
    IllegalZoneService illegalZoneService;

    @Test
    public void insert() {
        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setName("샘플1");
        illegalZone.setCode("1111100000");
        illegalZone.setIsDel(false);
//        illegalZone.setTypeSeq(1);
        illegalZoneService.set(illegalZone);
    }

    @Test
    public void insertList() {
        List<IllegalZone> illegalZones = Lists.newArrayList();

        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setName("샘플1");
        illegalZone.setCode("1111100001");
        illegalZone.setIsDel(false);
//        illegalZone.setTypeSeq(1);

        IllegalZone illegalZone2 = new IllegalZone();
        illegalZone2.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone2.setName("샘플2");
        illegalZone2.setCode("1111100002");
        illegalZone2.setIsDel(false);
//        illegalZone2.setTypeSeq(2);

        illegalZones.add(illegalZone);
        illegalZones.add(illegalZone2);

        illegalZoneService.sets(illegalZones);
    }

    @Test
    public void update() {
        illegalZoneService.modify(6, 3, "10:00", "11:00");
    }

    @Test
    public void delete() {
        illegalZoneService.delete(7);
    }


    @Test
    void select() {
        List<IllegalZone> illegalZones = illegalZoneService.gets();

        if( !illegalZones.isEmpty()) {
            IllegalZone illegalZone = illegalZones.get(0);

            GeometryFactory geometryFactory = new GeometryFactory();
            Point polyInnerPoint = geometryFactory.createPoint(new Coordinate(126.56866907996265,33.451180712229686));
            Point polyInnerPoint2 = geometryFactory.createPoint(new Coordinate(126.56915449563454,33.450893887400085));
            Point polyInnerPoint3 = geometryFactory.createPoint(new Coordinate(126.56826037812841,33.45117928982703));
            Point polyInnerPoint4 = geometryFactory.createPoint(new Coordinate(126.56816407185906,33.451079778302194));
            Point polyOuterPoint = geometryFactory.createPoint(new Coordinate(126.56916190732109,33.45157011423124));
            Point polyOuterPoint2 = geometryFactory.createPoint(new Coordinate(126.56652326588099,33.45011835549815));
            Point polyOuterPoint3 = geometryFactory.createPoint(new Coordinate(126.56601552728885,33.450567380165516));

            System.out.println("seq = " + illegalZone.getZoneSeq());
            System.out.println(illegalZone.getPolygon());

            Polygon polygon;
            try {
                polygon = (Polygon) new WKTReader().read(String.format(illegalZone.getPolygon()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            boolean innerWithin = polyInnerPoint.within     (polygon);
            boolean innerWithin2 = polyInnerPoint2.within   (polygon);
            boolean innerWithin3 = polyInnerPoint3.within   (polygon);
            boolean innerWithin4 = polyInnerPoint4.within   (polygon);
            boolean outerWithin = polyOuterPoint.within     (polygon);
            boolean outerWithin2 = polyOuterPoint2.within   (polygon);
            boolean outerWithin3 = polyOuterPoint3.within   (polygon);

            System.out.println("innerWithin :" + innerWithin);
            System.out.println("innerWithin2 :" + innerWithin2);
            System.out.println("innerWithin3 :" + innerWithin3);
            System.out.println("innerWithin4 :" + innerWithin4);
            System.out.println("outerWithin :" + outerWithin);
            System.out.println("outerWithin2 :" + outerWithin2);
            System.out.println("outerWithin3 :" + outerWithin3);
        }

    }
}
