package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.mapper.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.mapper.illegalzone.service.IllegalZoneService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-03-02
 * Author : zilet
 * Project : sbAdmin
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class AreaController extends ExtendsController {

    private final ObjectMapper objectMapper;

    private final IllegalZoneService illegalZoneService;

    private String subTitle = "불법주정차 구역";

    @GetMapping("/area")
    public RedirectView area(){
        return new RedirectView("/area/map");
    }

    @GetMapping("/area/map")
    public ModelAndView map() throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
//
//        List<IllegalZone> illegalZones = illegalZoneService.gets();
//
//        List<Integer> zoneSeqs = Lists.newArrayList();
//        List<Integer> zoneTypes = Lists.newArrayList();
//        List<String> polygons = Lists.newArrayList();
//
//        int index = 0;
//
//        for (IllegalZone illegalZone : illegalZones) {
//            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
//            StringBuilder builder = new StringBuilder();
//            int size = (polygon.getCoordinates().length -1);
//            for (Coordinate coordinate : polygon.getCoordinates() ) {
//                if ( size != index ) {
//                    builder.append(coordinate.getX() + " " + coordinate.getY() + ",");
//                } else {
//                    builder.append(coordinate.getX() + " " + coordinate.getY());
//                }
//                index ++;
//            }
//            polygons.add(builder.toString());
//            zoneTypes.add(illegalZone.getTypeSeq());
//            zoneSeqs.add(illegalZone.getZoneSeq());
//        }
//
//        modelAndView.addObject("zonePolygons", polygons);
//        modelAndView.addObject("zoneSeqs", zoneSeqs);
//        modelAndView.addObject("zoneTypes", zoneTypes);
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        modelAndView.setViewName(getPath("/map"));
        return modelAndView;
    }

}
