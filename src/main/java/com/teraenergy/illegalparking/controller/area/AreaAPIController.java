package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
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

    @PostMapping("/area/polygon/insert")
    @ResponseBody
    public String insertPolygon(@RequestBody Map<String, Object> param) {
        try {
            List<Map<String, Object>> polygons = (List<Map<String, Object>>) param.get("polygonData");

            List<IllegalZone> illegalZones = Lists.newArrayList();
            StringBuilder stringBuilder;

            for (Map<String, Object> dataMap : polygons) {
                List<Object> pointList = (List<Object>) dataMap.get("points");
                stringBuilder = new StringBuilder();
                stringBuilder.append("POLYGON((");
                int cnt = 1;
                int size = pointList.size();
                for (Object point : pointList) {
                    if (cnt != size) {
                        stringBuilder.append(((Map<String, String>) point).get("x"));
                        stringBuilder.append(" ");
                        stringBuilder.append(((Map<String, String>) point).get("y"));
                        stringBuilder.append(",");
                    } else {
                        stringBuilder.append(((Map<String, String>) point).get("x"));
                        stringBuilder.append(" ");
                        stringBuilder.append(((Map<String, String>) point).get("y"));
                        stringBuilder.append("))");
                    }
                }

                IllegalZone illegalZone = new IllegalZone();
                illegalZone.setPolygon(stringBuilder.toString());
                illegalZone.setName("");
                illegalZone.setTypeSeq(1);
                illegalZone.setCode("2");
                illegalZone.setIsDel(false);
                illegalZones.add(illegalZone);

                stringBuilder.setLength(0);
            }

            illegalZoneService.sets(illegalZones);

            return "success";
        } catch (Exception e) {
            return "fail";
        }


    }

    @PostMapping("/area/polygon/delete")
    @ResponseBody
    public String deletePolygon(@RequestBody Map<String, Object> paramMap) {
        try {
            illegalZoneService.delete((Integer) paramMap.get("zoneSeq"));
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @PostMapping("/area/polygon/update")
    @ResponseBody
    public String updatePolygon(@RequestBody Map<String, Object> paramMap) {
        try {
            illegalZoneService.modify((Integer) paramMap.get("zoneSeq"), (Integer) paramMap.get("zoneType"), (String) paramMap.get("startTime"), (String) paramMap.get("endTime"));
            Map<String, Object> resultMap = new HashMap<>();
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @PostMapping("/area/polygon")
    @ResponseBody
    public JsonNode polygon(HttpServletRequest request, @RequestBody String body) throws Exception {
        HashMap<String, Object> hashMap = objectMapper.convertValue(body, HashMap.class);
        String jsonStr = objectMapper.writeValueAsString(illegalZoneService.get((Integer) hashMap.get("zoneSeq")));
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/polygons")
    @ResponseBody
    public JsonNode polygons(HttpServletRequest request, @RequestBody String body) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(body);
        String jsonStr = objectMapper.writeValueAsString(_getZone(jsonNode));
        return objectMapper.readTree(jsonStr);
    }

    private HashMap<String, Object> _getParam(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return parameterMap;
    }

    private Map<String, Object> _getZone(JsonNode param) throws ParseException {
        String select = param.get("select").asText();
        List<IllegalZone> illegalZones = null;
        switch (select) {
            case "type":
                illegalZones = illegalZoneService.getsByType(param.get("typeSeq").asInt());
                break;
            case "dong":
                illegalZones = illegalZoneService.getsByDong(param.get("code").asText());
                break;
            case "typeAndDong":
                illegalZones = illegalZoneService.getsByTypeAndDong(param.get("typeSeq").asInt(), param.get("code").asText());
                break;
            case "all":
                illegalZones = illegalZoneService.gets();
                break;
        }

        List<Integer> zoneSeqs = Lists.newArrayList();
        List<Integer> zonetypes = Lists.newArrayList();
        List<String> polygons = Lists.newArrayList();

        for (IllegalZone illegalZone : illegalZones) {
            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
            StringBuilder builder = new StringBuilder();
            int first = 0;
            Coordinate firstCoordinate = null;
            for (Coordinate coordinate : polygon.getCoordinates()) {
                if ( first == 0) {
                    firstCoordinate = coordinate;
                }
                builder.append(coordinate.getX())
                        .append(" ")
                        .append(coordinate.getY()).append(",");
                first ++;
            }
            builder.append(firstCoordinate.getX())
                    .append(" ")
                    .append(firstCoordinate.getY());

            polygons.add(builder.toString());
            zonetypes.add(illegalZone.getTypeSeq());
            zoneSeqs.add(illegalZone.getZoneSeq());
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("zonePolygon", polygons);
        resultMap.put("zoneSeq", zoneSeqs);
        resultMap.put("zoneType", zonetypes);

        return resultMap;
    }
}
