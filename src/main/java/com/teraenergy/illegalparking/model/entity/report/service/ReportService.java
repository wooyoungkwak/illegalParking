package com.teraenergy.illegalparking.model.entity.report.service;

import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportOrderColumn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReportService {

    public Report get(Integer reportSeq);

    public List<Report> gets();

    public Page<Report> gets(int pageNumber, int pageSize, ReportFilterColumn filterColumn, String search, ReportOrderColumn orderColumn, Sort.Direction orderBy);

    public Report set(Report report);

    public List<Report> sets(List<Report> reports);

    public Report modify(Report report);

    public long remove(Integer reportSeq);

    public long removes(List<Integer> reportSeqs);
}
