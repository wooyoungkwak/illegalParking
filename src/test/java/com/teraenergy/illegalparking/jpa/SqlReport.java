package com.teraenergy.illegalparking.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
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
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class SqlReport {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private IllegalZoneMapperService illegalZoneMapperService;

    @Autowired
    private IllegalZoneService illegalZoneService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void insert(){
        try {
            insertByReceipt();
            insertByReport();
            insertByComment();
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertByReport() {
        List<Report> reports = Lists.newArrayList();

        Receipt firstReceipt = receiptService.get(1);
        Receipt secondReceipt = receiptService.get(2);

        Report report1 = new Report();
        report1.setFirstReceipt(firstReceipt);
        report1.setSecondReceipt(secondReceipt);
        report1.setRegDt(LocalDateTime.now());
        report1.setReportStateType(ReportStateType.COMPLETE);
        report1.setNote("");
        report1.setIsDel(false);
        reports.add(report1);

        reportService.sets(reports);
    }

    @Test
    public void insertByReceipt() throws TeraException {
        List<Receipt> receipts = Lists.newArrayList();

        IllegalZone illegalZone = illegalZoneService.get(1);
        User user = userService.get(2);

        Receipt receipt1 = new Receipt();
        receipt1.setIllegalZone(illegalZone);
        receipt1.setUser(user);
        receipt1.setRegDt(LocalDateTime.now().minusMinutes(3));
        receipt1.setCarNum("123가1234");
        receipt1.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt1.setCode("5013032000");
        receipt1.setReceiptStateType(ReceiptStateType.REPORT);
        receipt1.setIsDel(false);
        receipt1.setAddr("전라남도 나주시 빛가람동 상야1길 7");
        receipts.add(receipt1);

        Receipt receipt2 = new Receipt();
        receipt2.setIllegalZone(illegalZone);
        receipt2.setUser(user);
        receipt2.setRegDt(LocalDateTime.now());
        receipt2.setCarNum("123가1234");
        receipt2.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt2.setCode("5013032000");
        receipt2.setReceiptStateType(ReceiptStateType.PENALTY);
        receipt2.setIsDel(false);
        receipt2.setAddr("전라남도 나주시 산포면 신도리 378-4");
        receipts.add(receipt2);

        receiptService.sets(receipts);
    }

    @Test
    public void insertByComment() throws TeraException {
        Comment comment = new Comment();
        comment.setContent("댓글 테스트 ....");
        comment.setReceiptSeq(1);
        commentService.set(comment);
    }

    @Test
    public void update(){
//        User user;
//        try {
//            user = userService.get(1);
//        } catch (TeraException e) {
//            throw new RuntimeException(e);
//        }
//
//        Report report = reportService.get(2);
//        Receipt secondReceipt = report.getSecondReceipt();
//        long pointValue = secondReceipt.getIllegalZone().getIllegalEvent().getZoneGroupType().getValue();
//        Point point = new Point();
//        point.setPointType(PointType.PLUS);
//        point.setValue(pointValue);
//        point.setNote(report.getNote());
//        point.setUserSeq(user.getUserSeq());
//        point = pointService.set(point);
//
//        // 결재 등록
//        long currentPointValue = 0;
//        long beforePointValue = 0;
//        Calculate oldCalculate = calculateService.getAtLast(user.getUserSeq());
//
//        if (oldCalculate != null) {
//            currentPointValue = oldCalculate.getCurrentPointValue();
//            beforePointValue = currentPointValue;
//        }
//
//        currentPointValue += currentPointValue + point.getValue();
//
//        Calculate calculate = new Calculate();
//        calculate.setCurrentPointValue(currentPointValue);
//        calculate.setBeforePointValue(beforePointValue);
//        calculate.setPoint(point);
//        calculate.setUser(user);
//        calculate.setRegDt(LocalDateTime.now());
//        calculate.setIsDel(false);
//        calculateService.set(calculate);
//
//        report.setNote("테스트 ... 과태료대상이 맞습니다.");
//        report.setStateType(StateType.PENALTY);
//        report.setReportUserSeq(2);
//        report.setRegDt(LocalDateTime.now());
//        reportService.set(report);

    }

    @Test
    public void select(){


    }

    @Test
    public void selectByReport() {
        List<Report> reports = reportService.gets();
        try {
            objectMapper.writeValueAsString(reports);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectByReceipt(){
        Receipt receipt = receiptService.get(6);

        IllegalZone illegalZone = receipt.getIllegalZone();
        IllegalEvent illegalEvent = illegalZone.getIllegalEvent();

        if (illegalEvent == null) {
            System.out.println(" ***************** event is not ");
        } else {
            System.out.println(" *****************  " + illegalEvent.getEventSeq());
        }
    }

}
