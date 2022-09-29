package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;
import com.teraenergy.illegalparking.model.entity.calculate.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ResultType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
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
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final ObjectMapper objectMapper;

    private final ReportDtoService reportDtoService;

    private final PointService pointService;

    @PostMapping ("/report/get")
    @ResponseBody
    public JsonNode getReport(@RequestBody String body) {
        HashMap<String, Object> result = Maps.newHashMap();
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            Report report = reportService.get(reportSeq);
            report.getFirstReceipt();
            Receipt firstReceipt = receiptService.get(report.getFirstReceipt().getReceiptSeq());
            firstReceipt.getReceiptSeq();
            firstReceipt.getNote();

            ReportDto reportDto = reportDtoService.get(report);
            result.put("data", reportDto);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            result.put("success", isSuccess);
            return convertResult(result);
        }
    }

    @PostMapping ("/report/set")
    @ResponseBody
    public JsonNode setReport(@RequestBody String body) throws JsonProcessingException {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            String note = jsonNode.get("note").asText();
            Integer userSeq = jsonNode.get("userSeq").asInt();
            ResultType resultType = ResultType.valueOf(jsonNode.get("resultType").asText());

            Report report = reportService.get(reportSeq);

            report.setNote(note);
            report.setResultType(resultType);
            report.setReportUserSeq(userSeq);
            report.setRegDt(LocalDateTime.now());
            reportService.set(report);

            // point 적립
            long pointValue = report.getSecondReceipt().getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
            Point point = new Point();
            point.setReport(report);
            point.setPointType(PointType.PLUS);
            point.setValue(pointValue);
            point.setNote(report.getNote());
            point.setUserSeq(report.getReportUserSeq());

            pointService.set(point);
            isSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resultMap.put("success", isSuccess);
            return convertResult(resultMap);
        }
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
    public JsonNode setReceipt(@RequestBody String body){
        boolean isSuccess = false;
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
            receipt.setReceiptType(ReceiptType.REPORT);
            receipt.setIsDel(false);
            receipt.setAddr(addr);
            receiptService.set(receipt);

            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HashMap<String, Object> result = Maps.newHashMap();
            result.put("success", isSuccess);
            return convertResult(result);
        }
    }

    private JsonNode convertResult(HashMap<String, Object> map) {
        try {
            String jsonStr = objectMapper.writeValueAsString(map);
            return objectMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
