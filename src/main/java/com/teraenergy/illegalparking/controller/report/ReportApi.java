package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.calculate.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.StateType;
import com.teraenergy.illegalparking.model.dto.report.service.ReceiptDtoService;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
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
    private final PointService pointService;
    private final CalculateService calculateService;
    private final ReportDtoService reportDtoService;
    private final ReceiptDtoService receiptDtoService;

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
    public JsonNode setReport(@RequestBody String body) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        boolean isSuccess = false;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            Integer userSeq = jsonNode.get("userSeq").asInt();
            com.teraenergy.illegalparking.model.entity.report.enums.StateType stateType = com.teraenergy.illegalparking.model.entity.report.enums.StateType.valueOf(jsonNode.get("setResultType").asText());
            String note = jsonNode.get("note").asText();

            Report report = reportService.get(reportSeq);

            if ( report.getReportUserSeq() == null) {

            }

            if (stateType == com.teraenergy.illegalparking.model.entity.report.enums.StateType.PENALTY) {
                // point 적립
                long pointValue = report.getSecondReceipt().getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
//                Point point = new Point();
////                point.setReport(report);
//                point.setPointType(PointType.PLUS);
//                point.setValue(pointValue);
//                point.setNote(report.getNote());
//                point.setUserSeq(report.getReportUserSeq());
//                point = pointService.set(point);

                // 결재 등록
//                long currentPointValue = 0;
//                long beforePointValue = 0;
//                Calculate oldCalculate = calculateService.getAtLast(report.getReportUserSeq());
//
//                if (oldCalculate != null) {
//                    currentPointValue = oldCalculate.getCurrentPointValue();
//                    beforePointValue = currentPointValue;
//                }
//
//                currentPointValue += currentPointValue + point.getValue();
//
//                Calculate calculate = new Calculate();
//                User reportUser = userService.get(userSeq);
//                calculate.setCurrentPointValue(currentPointValue);
//                calculate.setBeforePointValue(beforePointValue);
//                calculate.setPoint(point);
//                calculate.setUser(reportUser);
//                calculate.setRegDt(LocalDateTime.now());
//                calculate.setIsDel(false);
//                calculateService.set(calculate);
            } else if (stateType == com.teraenergy.illegalparking.model.entity.report.enums.StateType.EXCEPTION) {
                // 신고 접수 에서 신고 제외 관련 내용 업데이트
                Receipt firstReceipt = report.getFirstReceipt();
                Receipt secondReceipt = report.getSecondReceipt();
                firstReceipt.setNote(note);
                firstReceipt.setStateType(StateType.EXCEPTION);
                secondReceipt.setNote(note);
                secondReceipt.setStateType(StateType.EXCEPTION);
                report.setFirstReceipt(firstReceipt);
                report.setSecondReceipt(secondReceipt);
            }

            report.setNote(note);
            report.setStateType(stateType);
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
     * 신고 접수
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

            // 1. 불법주정차 구역 내에 존재 여부 체크
            List<IllegalZone> illegalZones = illegalZoneMapperService.getsByGeometry(latitude, longitude);

            if (!illegalZones.isEmpty()) {
//                IllegalZone illegalZone = illegalZoneService.get(1);
                String regDtStr = jsonNode.get("regDt").asText().trim();
                String separate[] = regDtStr.split(" ");
                LocalDateTime regDt = LocalDateTime.parse(regDtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                IllegalZone realIllegalZone = null;

                // 2. 불법주정차 구역의 시간 체크
                for (IllegalZone illegalZone : illegalZones ) {
                    // 2-1. 첫번째 허용 적용 시간 ( 다음의 경우 불법주정차가 아님 )
                    if (illegalZone.getIllegalEvent().getUsedFirst()) {
                        LocalDateTime fsTime = LocalDateTime.parse(separate[0] + " " + illegalZone.getIllegalEvent().getFirstStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        LocalDateTime feTime = LocalDateTime.parse(separate[0] + " " + illegalZone.getIllegalEvent().getFirstEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        if ( !regDt.isBefore(fsTime)  && regDt.isBefore(feTime)) {
                            continue;
                        }
                    }

                    // 2-2. 두번째 허용 적용 시간 ( 다음의 경우 불법주정차가 아님 )
                    if ( illegalZone.getIllegalEvent().getUsedSecond()) {
                        LocalDateTime ssTime = LocalDateTime.parse( separate[0] + " " + illegalZone.getIllegalEvent().getSecondStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        LocalDateTime seTime = LocalDateTime.parse( separate[0] + " " + illegalZone.getIllegalEvent().getSecondEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        if ( !regDt.isBefore(ssTime) || regDt.isBefore(seTime)) {
                            continue;
                        }
                    }

                    realIllegalZone = illegalZone;
                }

                // 3. 신고 등록
                if ( realIllegalZone != null) {
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
                    receipt.setStateType(StateType.REPORT);
                    receipt.setIsDel(false);
                    receipt.setAddr(addr);
                    receipt.setRegDt(regDt);

                    receiptService.set(receipt);
                    isSuccess = true;
                }
            } else {
                result.put("msg", "불법주정차 구역이 아닙니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            result.put("success", isSuccess);
            return convertResult(result);
        }
    }

    @PostMapping("/api/receipt/gets")
    @ResponseBody
    public JsonNode getReceipt(@RequestBody String body) {
        HashMap<String, Object> resultMap = Maps.newHashMap();
        boolean isSuccess = false;
        List<ReceiptDto> receiptDtos = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            List<Receipt> receipts = receiptService.gets();
            receiptDtos = receiptDtoService.gets(receipts);
            isSuccess = true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            resultMap.put("success", isSuccess);
            if ( receiptDtos == null) receiptDtos = Lists.newArrayList();
            resultMap.put("receipts", receiptDtos);
            return convertResult(resultMap);
        }
    }

    private JsonNode convertResult(HashMap<String, Object> map) {
        try {
            return JsonUtil.toJsonNode(map);
        } catch (TeraException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
