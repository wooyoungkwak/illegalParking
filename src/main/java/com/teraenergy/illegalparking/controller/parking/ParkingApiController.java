package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class ParkingApiController {

    private final ObjectMapper objectMapper;

    private final ParkingService parkingService;

    @PostMapping("/parking")
    @ResponseBody
    public JsonNode parking(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Integer parkingSeq = jsonNode.get("parkingSeq").asInt();
        Parking parking = parkingService.get(parkingSeq);
        String jsonStr = objectMapper.writeValueAsString(parking);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/parkings")
    @ResponseBody
    public JsonNode parkings(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        return null;
    }

}
