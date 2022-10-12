package com.teraenergy.illegalparking.model.entity.report.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReportService {

    Report get(Integer reportSeq);

    List<Report> gets();

    Page<Report> gets(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search);

    int getSizeForReport(Integer governmentUserSeq);

    int getSizeForException(Integer governmentUserSeq);

    int getSizeForPenalty(Integer governmentUserSeq);

    int getSizeForCOMPLETE(LocationType locationType);

    Report set(Report report);

    List<Report> sets(List<Report> reports);

    Report modify(Report report);

    long remove(Integer reportSeq);

    long removes(List<Integer> reportSeqs);
}
