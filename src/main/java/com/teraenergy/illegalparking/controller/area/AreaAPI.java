package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.illegalzone.service.IllegalZoneDtoService;
import com.teraenergy.illegalparking.model.dto.illegalzone.service.PointDtoService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
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
public class AreaAPI {

    private final IllegalZoneService illegalZoneService;

    private final IllegalZoneMapperService illegalZoneMapperService;

    private final IllegalZoneDtoService illegalZoneDtoService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final IllegalEventService illegalEventService;

    private final PointDtoService pointDtoService;

    private final PointService pointService;

    @PostMapping("/area/markers")
    @ResponseBody
    public Object markers(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws TeraException {
        return "";
    }

    @PostMapping("/area/coordinates")
    @ResponseBody
    public Object coordinates(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws TeraException {
        return "";
    }

    @PostMapping("/area/zone/get")
    @ResponseBody
    public Object getZone(@RequestBody String body) throws Exception {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer zoneSeq = jsonNode.get("zoneSeq").asInt();
        IllegalZone illegalZone = illegalZoneMapperService.get(zoneSeq);
        if(illegalZone.getEventSeq() != null) {
            IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
            illegalZone.setIllegalEvent(illegalEvent);
        }
        return illegalZone;
    }

    @PostMapping("/area/zone/gets")
    @ResponseBody
    public Object getsZone(HttpServletRequest request, @RequestBody String body) throws Exception {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        return _getZone(jsonNode);
    }

    @PostMapping("/area/zone/set")
    @ResponseBody
    public Object setZone(@RequestBody Map<String, Object> param) throws TeraException {
        try {
            List<Map<String, Object>> polygons = (List<Map<String, Object>>) param.get("polygonData");
            List<IllegalZone> illegalZones = Lists.newArrayList();
            StringBuilder stringBuilder;

            for (Map<String, Object> dataMap : polygons) {
                List<Object> pointList = (List<Object>) dataMap.get("points");
                stringBuilder = new StringBuilder();
                stringBuilder.append("POLYGON((");

                for (Object point : pointList) {
                    stringBuilder.append(((Map<String, Long>) point).get("x"));
                    stringBuilder.append(" ");
                    stringBuilder.append(((Map<String, Long>) point).get("y"));
                    stringBuilder.append(",");
                }

                stringBuilder.append(((Map<String, Long>) pointList.get(0)).get("x"));
                stringBuilder.append(" ");
                stringBuilder.append(((Map<String, Long>) pointList.get(0)).get("y"));
                stringBuilder.append("))");

                IllegalZone illegalZone = new IllegalZone();
                illegalZone.setPolygon(stringBuilder.toString());
//                illegalZone.setName("");
//                illegalZone.setIllegalTypeSeq(Integer.parseInt((String) param.get("illegalType")));
                illegalZone.setCode((String) dataMap.get("code"));
                illegalZone.setIsDel(false);
                illegalZones.add(illegalZone);

                stringBuilder.setLength(0);
            }

            return  illegalZoneMapperService.sets(illegalZones);

        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.ZONE_CREATE_FAIL, e);
        }
    }

    @PostMapping("/area/zone/modify")
    @ResponseBody
    public Object modifyZone(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            IllegalZone illegalZone = illegalZoneMapperService.get(jsonNode.get("zoneSeq").asInt());
            IllegalEvent illegalEvent = new IllegalEvent();
            illegalEvent.setIllegalType(
                    IllegalType.valueOf(jsonNode.get("illegalType").asText()));
            illegalEvent.setName(jsonNode.get("name").asText());
            illegalEvent.setUsedFirst(jsonNode.get("usedFirst").asBoolean());
            illegalEvent.setFirstStartTime(jsonNode.get("firstStartTime").asText());
            illegalEvent.setFirstEndTime(jsonNode.get("firstEndTime").asText());
            illegalEvent.setUsedSecond(jsonNode.get("usedSecond").asBoolean());
            illegalEvent.setSecondStartTime(jsonNode.get("secondStartTime").asText());
            illegalEvent.setSecondEndTime(jsonNode.get("secondEndTime").asText());
            if (illegalZone.getEventSeq() != null) {
                illegalEvent.setEventSeq(illegalZone.getEventSeq());
            }
            illegalEvent = illegalEventService.set(illegalEvent);
            illegalZoneMapperService.modifyByEvent(jsonNode.get("zoneSeq").asInt(), illegalEvent.getEventSeq());
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.ZONE_MODIFY_FAIL, e);
        }

        return "";
    }

    @PostMapping("/area/zone/remove")
    @ResponseBody
    public Object removeZone(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        illegalZoneMapperService.delete(jsonNode.get("zoneSeq").asInt());
        return "";
    }

    @PostMapping("/area/group/set")
    @ResponseBody
    public Object setGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        IllegalGroup illegalGroup = new IllegalGroup();
        illegalGroup.setName(jsonNode.get("name").asText());
        illegalGroup.setLocationType(LocationType.valueOf(jsonNode.get("locationType").asText()));
        return illegalGroupServcie.set(illegalGroup);
    }

    @PostMapping("/area/point/get")
    @ResponseBody
    public Object getPoint(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        return pointDtoService.gets(jsonNode.get("groupSeq").asInt());
    }

    @PostMapping("/area/point/set")
    @ResponseBody
    public Object setPoint(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);

        Point point = new Point();
        point.setPointType(PointType.valueOf(jsonNode.get("pointType").asText()));
        point.setLimitValue(jsonNode.get("limitValue").asLong());
        point.setValue(jsonNode.get("value").asLong());
        point.setStartDate(StringUtil.convertStringToDate(jsonNode.get("startDate").asText(), "yyyy-MM-dd"));
        point.setStopDate(StringUtil.convertStringToDate(jsonNode.get("stopDate").asText(), "yyyy-MM-dd"));
        point.setIsPointLimit(jsonNode.get("isPointLimit").asBoolean());
        point.setIsTimeLimit(jsonNode.get("isTimeLimit").asBoolean());
        point.setGroupSeq(jsonNode.get("groupSeq").asInt());
        point = pointService.set(point);

        return pointDtoService.get(point);
    }

    @PostMapping(value = "/area/event/group/name/get")
    @ResponseBody
    public Object getName(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        return illegalGroupServcie.gets(locationType);
    }

    private Map<String, Object> _getZone(JsonNode param) throws ParseException {
        String select = param.get("select").asText();
        List<String> codes = Lists.newArrayList();
        if ("dong".equals(select) || "typeAndDong".equals(select)) {
            JsonNode codesArrNode = param.get("codes");
            if (codesArrNode.isArray()) {
                for (JsonNode obj : codesArrNode) {
                    codes.add(obj.asText());
                }
            }
        }

        List<IllegalZone> illegalZones = null;
        switch (select) {
            case "type":
                illegalZones = illegalZoneMapperService.getsByIllegalType(param.get("illegalType").asText());
                break;
            case "dong":
                illegalZones = illegalZoneMapperService.getsByCode(codes);
                break;
            case "typeAndDong":
                illegalZones = illegalZoneMapperService.getsByIllegalTypeAndCode(param.get("illegalType").asText(), codes);
                break;
            default:
                illegalZones = illegalZoneMapperService.gets();
                break;
        }

        List<Integer> zoneSeqs = Lists.newArrayList();
        List<String> zoneTypes = Lists.newArrayList();
        List<String> polygons = Lists.newArrayList();

        for (IllegalZone illegalZone : illegalZones) {
            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
            StringBuilder builder = new StringBuilder();
            int first = 0;
            Coordinate firstCoordinate = null;
            for (Coordinate coordinate : polygon.getCoordinates()) {
                if (first == 0) {
                    firstCoordinate = coordinate;
                }
                builder.append(coordinate.getX())
                        .append(" ")
                        .append(coordinate.getY()).append(",");
                first++;
            }
            builder.append(firstCoordinate.getX())
                    .append(" ")
                    .append(firstCoordinate.getY());

            polygons.add(builder.toString());
            if (illegalZone.getEventSeq() == null) zoneTypes.add("");
            else zoneTypes.add(illegalEventService.get(illegalZone.getEventSeq()).getIllegalType().toString());
            zoneSeqs.add(illegalZone.getZoneSeq());
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("zonePolygons", polygons);
        resultMap.put("zoneSeqs", zoneSeqs);
        resultMap.put("zoneTypes", zoneTypes);

        return resultMap;
    }
}
