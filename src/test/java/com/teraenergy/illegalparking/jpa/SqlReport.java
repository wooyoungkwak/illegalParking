package com.teraenergy.illegalparking.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;
import com.teraenergy.illegalparking.model.entity.calculate.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.calculate.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ResultType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
//@Transactional
public class SqlReport {

    @Autowired
    private ReportService reportService;


    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private IllegalZoneMapperService illegalZoneMapperService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CalculateService calculateService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void insert() {
        List<Report> reports = Lists.newArrayList();

        Receipt firstReceipt = receiptService.get(1);
        Receipt secondReceipt = receiptService.get(2);

        Report report1 = new Report();
        report1.setFirstReceipt(firstReceipt);
        report1.setSecondReceipt(secondReceipt);
        report1.setRegDt(LocalDateTime.now());
        report1.setResultType(ResultType.WAIT);
        report1.setNote("");
        report1.setZoneSeq(1);
        report1.setCode(secondReceipt.getCode());
        report1.setIsDel(false);
        reports.add(report1);

        reportService.sets(reports);
    }

    @Test
    public void update(){
        User user;
        try {
            user = userService.get(1);
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }

        Report report = reportService.get(2);
        Receipt secondReceipt = report.getSecondReceipt();
        long pointValue = secondReceipt.getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
        Point point = new Point();
        point.setReport(report);
        point.setPointType(PointType.PLUS);
        point.setValue(pointValue);
        point.setNote(report.getNote());
        point.setUserSeq(user.getUserSeq());
        point = pointService.set(point);

        // 결재 등록
        long currentPointValue = 0;
        long beforePointValue = 0;
        Calculate oldCalculate = calculateService.getAtLast(user.getUserSeq());

        if (oldCalculate != null) {
            currentPointValue = oldCalculate.getCurrentPointValue();
            beforePointValue = currentPointValue;
        }

        currentPointValue += currentPointValue + point.getValue();

        Calculate calculate = new Calculate();
        calculate.setCurrentPointValue(currentPointValue);
        calculate.setBeforePointValue(beforePointValue);
        calculate.setPoint(point);
        calculate.setUser(user);
        calculate.setRegDt(LocalDateTime.now());
        calculate.setIsDel(false);
        calculateService.set(calculate);

        report.setNote("테스트 ... 과태료대상이 맞습니다.");
        report.setResultType(ResultType.PENALTY);
        report.setReportUserSeq(2);
        report.setRegDt(LocalDateTime.now());
        reportService.set(report);

    }

    @Test
    public void select(){

        List<Report> reports = reportService.gets();
        try {
            objectMapper.writeValueAsString(reports);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
