package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class ParkingApi {

    private final ObjectMapper objectMapper;

    private final ParkingService parkingService;

    @PostMapping("/parking/get")
    @ResponseBody
    public JsonNode getParking(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Integer parkingSeq = jsonNode.get("parkingSeq").asInt();
        Parking parking = parkingService.get(parkingSeq);
        String jsonStr = objectMapper.writeValueAsString(parking);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/parking/gets")
    @ResponseBody
    public JsonNode getsParking(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        List<String> codes = Lists.newArrayList();
        JsonNode codesArrNode = jsonNode.get("codes");
        if(codesArrNode.isArray()) {
            for (JsonNode obj : codesArrNode) {
                codes.add(obj.asText());
            }
        }
        List<Parking> parkings = parkingService.gets(codes);
        String jsonStr = objectMapper.writeValueAsString(parkings);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/parking/set")
    @ResponseBody
    public JsonNode setParking(@RequestBody String body) throws JsonProcessingException {
//        JsonNode jsonNode = objectMapper.readTree(body);
        Parking parking = objectMapper.convertValue(body, Parking.class);
        parkingService.set(parking);
        return null;
    }

}
