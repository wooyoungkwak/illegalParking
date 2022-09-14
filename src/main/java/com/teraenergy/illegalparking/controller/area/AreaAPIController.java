package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.mapper.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.mapper.illegalzone.service.IllegalZoneService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class AreaAPIController {

    private final ObjectMapper objectMapper;

    private final IllegalZoneService illegalZoneService;

    @PostMapping("/area/markers")
    @ResponseBody
    public JsonNode markers(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws JsonProcessingException {
        HashMap<String, String> result = Maps.newHashMap();

        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/coordinates")
    @ResponseBody
    public JsonNode coordinates(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws JsonProcessingException {
        HashMap<String, String> result = Maps.newHashMap();

        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }


    @PostMapping("/insertPolygon")
    public Map<String, Object> insertPolygon(@RequestBody Map<String, Object> param) throws Exception {
        List<Map<String,Object>> polygons = (List<Map<String, Object>>) param.get("polygonData");
        for (Map<String, Object> dataMap : polygons) {
            System.out.println(dataMap);
            System.out.println(dataMap.get("points"));

            List<Object> pointList = (List<Object>) dataMap.get("points");
            System.out.println(pointList.get(0));
            pointList.add(pointList.get(0));

            dataMap.put("points", pointList);
            dataMap.put("zoneType", "N");
//            illegalZoneService.set(dataMap, PAGE_ID + PROGRAM_ID + ".insertPolygon");
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("polygonList", polygonList(param).get("polygonList"));
        return resultMap;
    }

    @DeleteMapping("/deletePolygon")
    public Map<String, Object> deletePolygon(@RequestBody Map<String, Object> paramMap) throws Exception {
//        commonService.deleteContents(paramMap, PAGE_ID + PROGRAM_ID + ".deletePolygon");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("polygonList", polygonList(paramMap).get("polygonList"));
        return resultMap;
    }

    @PutMapping("/updatePolygon")
    public Map<String, Object> updatePolygon(@RequestBody Map<String, Object> paramMap) throws Exception {
//        commonService.updateContents(paramMap, PAGE_ID + PROGRAM_ID + ".updatePolygon");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("polygonList", polygonList(paramMap).get("polygonList"));
        return resultMap;
    }

    @GetMapping("/polygonDetail")
    public Map<String, Object> polygonDetail(@RequestParam int polySeq) throws Exception {
//        Map<String, Object> result = (Map<String, Object>) commonService.selectContents(polySeq, PAGE_ID + PROGRAM_ID + ".selectPolygonDetail");
//        illegalZoneService.gets()
        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("result", result);
        resultMap.put("success", "ok");
        return resultMap;
    }

    @GetMapping("/polygonList")
    public Map<String, Object> polygonList(@RequestParam Map<String, Object> searchParam) throws Exception {
        List<IllegalZone> illegalZones = illegalZoneService.gets();

        List<Integer> zoneSeqs = Lists.newArrayList();

//        Map<String, String> polygonList = Maps.newHashMap();
        List<String> polygons = Lists.newArrayList();

        int index = 0;
        for (IllegalZone illegalZone : illegalZones) {
            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
            StringBuilder builder = new StringBuilder();
            for (Coordinate coordinate : polygon.getCoordinates() ) {
                builder.append(Double.toString(coordinate.getX()) + " " + Double.toString(coordinate.getY()) + ",");
            }
            polygons.add(builder.toString());
        }


        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("polygonList", polygonList);
        resultMap.put("success", "ok");
        return resultMap;
    }

}
