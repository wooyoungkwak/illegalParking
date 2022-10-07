package com.teraenergy.illegalparking.model.entity.report.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.report.domain.QReport;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.StateType;
import com.teraenergy.illegalparking.model.entity.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final JPAQueryFactory jpaQueryFactory;

    private final ReportRepository reportRepository;

    @Override
    public Report get(Integer reportSeq) {
        return reportRepository.findByReportSeq(reportSeq);
    }

    @Override
    public List<Report> gets() {
        return reportRepository.findAll();
    }

    @Override
    public Page<Report> gets(int pageNumber, int pageSize, StateType stateType, ReportFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case CAR_NUM:
                    query.where(QReport.report.secondReceipt.carNum.eq(search));
                    break;
                case ADDR:
                    query.where(QReport.report.secondReceipt.addr.contains(search));
                    break;
                case USER:
                    query.where(QReport.report.secondReceipt.user.name.contains(search));
                    break;
            }
        }

        query.where(QReport.report.isDel.isFalse());

        int total = query.fetch().size();

        if (stateType != null) {
            query.where(QReport.report.stateType.eq(stateType));
        }

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Report> reports = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Report> page = new PageImpl<Report>(reports, pageRequest, total);
        return page;
    }

    @Override
    public Report set(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public List<Report> sets(List<Report> reports) {
        return reportRepository.saveAll(reports);
    }

    @Override
    public Report modify(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public long remove(Integer reportSeq) {
        JPAUpdateClause query = jpaQueryFactory.update(QReport.report);
        query.set(QReport.report.isDel, true);
        query.where(QReport.report.reportSeq.eq(reportSeq));
        return query.execute();
    }

    @Override
    public long removes(List<Integer> reportSeqs) {
        JPAUpdateClause query = jpaQueryFactory.update(QReport.report);
        query.set(QReport.report.isDel, true);
        query.where(QReport.report.reportSeq.in(reportSeqs));
        return query.execute();
    }
}
