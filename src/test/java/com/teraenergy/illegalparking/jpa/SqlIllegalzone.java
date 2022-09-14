package com.teraenergy.illegalparking.jpa;


import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.mapper.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.mapper.illegalzone.service.IllegalZoneService;
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
        illegalZone.setName("샘플2");
        illegalZone.setDongSeq(2);
        illegalZone.setIsDel(false);
        illegalZoneService.set(illegalZone);

//        Polygon polygon = null;
//        try {
//            polygon = (Polygon) new WKTReader().read(String.format(ilZone.getPolygon()));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

//        Coordinate[] coordinates = polygon.getCoordinates();
//        for (Coordinate coordinate : coordinates) {
//            System.out.print(coordinate.getX());
//            System.out.print(":");
//            System.out.println(coordinate.getY());
//        }
//        IllegalZone illegalZone = new IllegalZone();
//        illegalZone.setName("샘플");
//        illegalZone.setLawDongSeq(1);
//        illegalZone.setPolygon(polygon);
//        illegalZone.setIsDel(false);
//        illegalZoneService.set(illegalZone);
    }

    @Test
    void select() {
        List<IllegalZone> illegalZones = illegalZoneService.gets(1);
//        List<IlZone> illegalZones = iZoneService.gets();

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

            System.out.println(illegalZone.getPolygon());

            Polygon polygon = null;
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
