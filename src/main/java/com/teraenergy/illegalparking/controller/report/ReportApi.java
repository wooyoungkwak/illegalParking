package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;
import com.teraenergy.illegalparking.model.entity.calculate.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.calculate.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReportApi {


    private final ObjectMapper objectMapper;
    private final IllegalZoneService illegalZoneService;

    private final IllegalZoneMapperService illegalZoneMapperService;
    private final UserService userService;
    private final ReceiptService receiptService;
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final ReportDtoService reportDtoService;
    private final PointService pointService;
    private final CalculateService calculateService;

    @PostMapping("/report/get")
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

    @PostMapping("/report/set")
    @ResponseBody
    public JsonNode setReport(@RequestBody String body) throws JsonProcessingException {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            Integer userSeq = jsonNode.get("userSeq").asInt();
            ResultType resultType = ResultType.valueOf(jsonNode.get("setResultType").asText());
            String note = jsonNode.get("note").asText();

            Report report = reportService.get(reportSeq);

            if (resultType == ResultType.PENALTY) {
                // point 적립
                long pointValue = report.getSecondReceipt().getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
                Point point = new Point();
                point.setReport(report);
                point.setPointType(PointType.PLUS);
                point.setValue(pointValue);
                point.setNote(report.getNote());
                point.setUserSeq(report.getReportUserSeq());
                point = pointService.set(point);

                // 결재 등록
                long currentPointValue = 0;
                long beforePointValue = 0;
                Calculate oldCalculate = calculateService.getAtLast(report.getReportUserSeq());

                if (oldCalculate != null) {
                    currentPointValue = oldCalculate.getCurrentPointValue();
                    beforePointValue = currentPointValue;
                }

                currentPointValue += currentPointValue + point.getValue();

                Calculate calculate = new Calculate();
                User reportUser = userService.get(report.getReportUserSeq());
                calculate.setCurrentPointValue(currentPointValue);
                calculate.setBeforePointValue(beforePointValue);
                calculate.setPoint(point);
                calculate.setUser(reportUser);
                calculate.setRegDt(LocalDateTime.now());
                calculate.setIsDel(false);
                calculateService.set(calculate);
            } else if (resultType == ResultType.EXCEPTION) {
                // 신고 접수 에서 신고 제외 관련 내용 업데이트
                Receipt firstReceipt = report.getFirstReceipt();
                Receipt secondReceipt = report.getSecondReceipt();
                firstReceipt.setNote(note);
                firstReceipt.setReceiptType(ReceiptType.EXCEPTION);
                secondReceipt.setNote(note);
                secondReceipt.setReceiptType(ReceiptType.EXCEPTION);
                report.setFirstReceipt(firstReceipt);
                report.setSecondReceipt(secondReceipt);
            }

            report.setNote(note);
            report.setResultType(resultType);
            report.setReportUserSeq(userSeq);
            report.setRegDt(LocalDateTime.now());
            reportService.set(report);
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
     * longitude :
     * fileName :
     */
    @PostMapping("/api/receipt/set")
    @ResponseBody
    public JsonNode setReceipt(@RequestBody String body) {
        HashMap<String, Object> result = Maps.newHashMap();
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            String latitude = jsonNode.get("latitude").asText(); // 위도
            String longitude = jsonNode.get("longitude").asText(); // 경도

            // 1. 불법주정차 구역 내에 존재 여부
            List<IllegalZone> illegalZones = illegalZoneMapperService.getsByGeometry(latitude, longitude);

            if (!illegalZones.isEmpty()) {
//                IllegalZone illegalZone = illegalZoneService.get(1);
                String regDtStr = jsonNode.get("regDt").asText().trim();
                String separate[] = regDtStr.split(" ");
                LocalDateTime regDt = LocalDateTime.parse(regDtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                IllegalZone realIllegalZone = null;

                // 2. 불법주정차 구역의 Event
                for (IllegalZone illegalZone : illegalZones ) {
                    if (illegalZone.getIllegalEvent().getUsedFirst()) {
                        LocalDateTime fsTime = LocalDateTime.parse(separate[0] + " " + illegalZone.getIllegalEvent().getFirstStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        LocalDateTime feTime = LocalDateTime.parse(separate[0] + " " + illegalZone.getIllegalEvent().getFirstEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        if ( regDt.isBefore(fsTime)) {
                            realIllegalZone = illegalZone;
//                        } else if (regDt.isAfter(feTime)) {

                        }
                    }

                    if ( illegalZone.getIllegalEvent().getUsedSecond()) {
                        LocalDateTime ssTime = LocalDateTime.parse( separate[0] + " " + illegalZone.getIllegalEvent().getSecondStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        LocalDateTime seTime = LocalDateTime.parse( separate[0] + " " + illegalZone.getIllegalEvent().getSecondEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        if ( !regDt.isBefore(ssTime) || regDt.isBefore(seTime)) {
                            realIllegalZone = illegalZone;
                        }
                    }
                }



                User user = userService.get(jsonNode.get("id").asText());
                String addr = jsonNode.get("addr").asText();
                String addrParts[] = addr.split(" ");
                LawDong lawDong = lawDongService.getFromLnmadr(addrParts[0] + " " + addrParts[1] + " " + addrParts[2]);


                Receipt receipt = new Receipt();
                receipt.setIllegalZone(realIllegalZone);
                receipt.setUser(user);
                receipt.setCarNum(jsonNode.get("carNum").asText());
                receipt.setFileName(jsonNode.get("fileName").asText());
                receipt.setCode(lawDong.getCode());
                receipt.setReceiptType(ReceiptType.REPORT);
                receipt.setIsDel(false);
                receipt.setAddr(addr);
                receipt.setRegDt(regDt);
                
                receiptService.set(receipt);
                isSuccess = true;
            } else {
                result.put("msg", "불법주정차 구역이 아닙니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
