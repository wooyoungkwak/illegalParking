package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ResultType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
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
@Transactional
public class SqlReport {

    @Autowired
    private ReportService reportService;


    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private IllegalZoneService illegalZoneService;

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
        report1.setNote("테스트 ... ");
        report1.setZoneSeq(1);
        report1.setCode("5013032000");
        report1.setIsDel(false);
        reports.add(report1);

        reportService.sets(reports);
    }

    @Test
    public void select(){


    }

    @Test
    public void update(){

    }
}
