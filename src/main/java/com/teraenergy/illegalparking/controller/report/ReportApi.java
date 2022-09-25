package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptState;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class ReportApi {


    private final IllegalZoneJpaService illegalZoneJpaService;
    private final UserService userService;
    private final ReceiptService receiptService;

    private final LawDongService lawDongService;

    private final ObjectMapper objectMapper;

    @PostMapping ("/report/get")
    @ResponseBody
    public JsonNode getReport(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        return  null;
    }


    /**
     * id :
     * carNum :
     * addr :
     * latitude :
     * longtitude :
     * fileName :
    */
    @PostMapping("/recepit/set")
    @ResponseBody
    public JsonNode setReceipt(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            IllegalZone illegalZone = illegalZoneJpaService.get(1);
            User user = userService.getByDB(jsonNode.get("id").asText());
            String addr = jsonNode.get("addr").asText();
            String addrParts[] = addr.split(" ");
            LawDong lawDong = lawDongService.getFromLnmadr(addrParts[0] + " " + addrParts[1] + " " + addrParts[2]);

            Receipt receipt = new Receipt();
            receipt.setIllegalZone(illegalZone);
            receipt.setUser(user);
            receipt.setRegDt(LocalDateTime.now());
            receipt.setCarNum(jsonNode.get("carNum").asText());
            receipt.setFileName(jsonNode.get("fileName").asText());
            receipt.setCode(lawDong.getCode());
            receipt.setReceiptState(ReceiptState.ING);
            receipt.setIsDel(false);
            receipt.setAddr(addr);
            receiptService.set(receipt);

            HashMap<String, Object> result = Maps.newHashMap();
            result.put("success", true);
            String jsonStr = objectMapper.writeValueAsString(result);
            return objectMapper.readTree(jsonStr);
        } catch (Exception e) {
            throw new TeraException(e);
        }
    }
}
