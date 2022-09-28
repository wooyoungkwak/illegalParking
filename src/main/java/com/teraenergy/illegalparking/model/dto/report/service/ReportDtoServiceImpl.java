package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Date : 2022-09-28
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReportDtoServiceImpl implements ReportDtoService {

    @Override
    public ReportDto get(Report report) {
        ReportDto reportDto = new ReportDto();
        Receipt firstReceipt = report.getFirstReceipt();
        Receipt secondReceipt = report.getSecondReceipt();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

        reportDto.setFirstAddr(firstReceipt.getAddr());
        reportDto.setFirstCarNum(firstReceipt.getCarNum());
        reportDto.setFirstRegDt(firstReceipt.getRegDt().format(formatter));
        reportDto.setFirstFileName(firstReceipt.getFileName());
        reportDto.setFirstIllegalType(firstReceipt.getIllegalZone().getIllegalType());

        reportDto.setSecondAddr(secondReceipt.getAddr());
        reportDto.setSecondCarNum(secondReceipt.getCarNum());
        reportDto.setSecondRegDt(secondReceipt.getRegDt().format(formatter));
        reportDto.setSecondFileName(secondReceipt.getFileName());
        reportDto.setSecondIllegalType(secondReceipt.getIllegalZone().getIllegalType());

        reportDto.setNote(report.getNote());
        reportDto.setResult(report.getResult());
        return reportDto;
    }

    @Override
    public List<ReportDto> gets(List<Report> reports) {
        return null;
    }
}
