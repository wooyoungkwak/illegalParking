package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;

import java.util.List;

/**
 * Date : 2022-09-28
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ReportDtoService {

    ReportDto get(Report report);

    List<ReportDto> gets(List<Report> reports);
}
