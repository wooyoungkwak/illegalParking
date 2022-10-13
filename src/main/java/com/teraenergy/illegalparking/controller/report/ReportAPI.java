package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDetailDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDetailDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReportAPI {

    private final IllegalZoneService illegalZoneService;
    private final IllegalZoneMapperService illegalZoneMapperService;
    private final UserService userService;
    private final ReceiptService receiptService;
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final PointService pointService;
    private final CalculateService calculateService;
    private final ReportDtoService reportDtoService;

    private final CommentService commentService;

    @PostMapping("/report/report/get")
    @ResponseBody
    public Object getReport(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer reportSeq = jsonNode.get("reportSeq").asInt();
        return reportDtoService.getFromReportDetailDto(reportSeq);
    }

    @PostMapping("/report/report/set")
    @ResponseBody
    public Object setReport(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);

        return null;
    }

    @PostMapping("/report/receipt/get")
    @ResponseBody
    public Object getReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer receiptSeq = jsonNode.get("receiptSeq").asInt();
        return reportDtoService.getFromReceiptDetailDto(receiptSeq);
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
    public Object setReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        double latitude = jsonNode.get("latitude").asDouble();      // 위도
        double longitude = jsonNode.get("longitude").asDouble();    // 경도
        IllegalZone illegalZone = illegalZoneMapperService.get(latitude, longitude);
        String carNum = jsonNode.get("carNum").asText();
        String regDtStr = jsonNode.get("regDt").asText();
        LocalDateTime regDt = StringUtil.convertStringToDateTime(regDtStr, "yyyy-MM-dd HH:mm");

        String addr = jsonNode.get("addr").asText();
        String temp[] = addr.split(" ");
        LawDong lawDong = lawDongService.getFromLnmadr(temp[0] + " " + temp[1] + " " + temp[2]);
        Receipt receipt = new Receipt();
        receipt.setAddr(addr);
        receipt.setCarNum(carNum);
        receipt.setFileName(jsonNode.get("fileName").asText());
        receipt.setRegDt(regDt);
        receipt.setCode(lawDong.getCode());
        receipt = receiptService.set(receipt);

        Comment comment = new Comment();
        // 1. 불법 주정자 지역 체크
        if (illegalZone == null) {
            _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA);
        }

        // 2. 불법 주정차 시간 체크
        String dateStr = regDtStr.split(" ")[0];
        if ( illegalZone.getIllegalEvent().getUsedFirst()) {
            LocalDateTime fs = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime fe = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstEndTime(), "yyyy-MM-dd HH:mm");
            if (fs.isAfter(regDt) && fe.isBefore(regDt)) {
                _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }

        if ( illegalZone.getIllegalEvent().getUsedSecond()) {
            LocalDateTime ss = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime se = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondEndTime(), "yyyy-MM-dd HH:mm");

            if (ss.isAfter(regDt) && se.isBefore(regDt)) {
                _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }

        // 3. 이미 신고된 차량 여부 체크
        if ( reportService.isExist(carNum) ) {
            _comment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM);
        }

        return "";
    }

    @PostMapping("/api/receipt/gets")
    @ResponseBody
    public Object getReceiptByApi(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);

        return null;
    }

    
    // 불법 주정차 신고 코멘트 저장
    public void _comment(Integer receiptSeq, String content) {
        Comment comment = new Comment();
        comment.setReceiptSeq(receiptSeq);
        comment.setRegDt(LocalDateTime.now());
        comment.setContent(content);
        commentService.set(comment);
    }

}
