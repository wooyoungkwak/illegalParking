package com.teraenergy.illegalparking.model.entity.report.service;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.QIllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.report.domain.QReport;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.repository.ReportRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public boolean isExist(String carNum, IllegalType illegalType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = null;
        switch (illegalType) {
            case FIVE_MINUTE:   // 5분 주정차
                startTime = now.minusMinutes(16);
                break;
            case ILLEGAL:       // 불법 주정차
                startTime = now.minusMinutes(11);
                break;
        }
        LocalDateTime endTime = now;

        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.carNum.eq(carNum));
        query.where(QReport.report.receipt.receiptStateType.eq(ReceiptStateType.OCCUR));
        query.where(QReport.report.receipt.regDt.between(startTime, endTime));
        query.where(QReport.report.isDel.isFalse());
        if (query.fetchOne() == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isExistByReceipt(String carNum, LocalDateTime regDt, IllegalType illegalType) {
        LocalDateTime startTime = null;
        switch (illegalType) {
            case ILLEGAL:
                startTime = regDt.minusMinutes(11);
                break;
            case FIVE_MINUTE:
                startTime = regDt.minusMinutes(16);
                break;
        }
        LocalDateTime endTime = regDt;

        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.carNum.eq(carNum));
        query.where(QReport.report.receipt.secondRegDt.between(startTime, endTime));
        if (query.fetch().size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Report get(Integer reportSeq) {
        return reportRepository.findByReportSeq(reportSeq);
    }

    @Override
    public List<Report> gets() {
        return reportRepository.findAll();
    }

    @Override
    public Page<Report> gets(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case CAR_NUM:
                    query.where(QReport.report.receipt.carNum.contains(search));
                    break;
                case ADDR:
                    query.where(QReport.report.receipt.addr.contains(search));
                    break;
                case USER:
                    query.where(QReport.report.receipt.user.name.contains(search));
                    break;
            }
        }

        query.where(QReport.report.isDel.isFalse());

        if (reportStateType != null) {
            query.where(QReport.report.reportStateType.eq(reportStateType));
        }

        int total = query.fetch().size();

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Report> reports = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Report> page = new PageImpl<Report>(reports, pageRequest, total);
        return page;
    }

    @Override
    public int getSizeForReport(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        return query.fetch().size();
    }

    // 신고제외(미처리) 처리 신고 건수

    @Override
    public int getSizeForException(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.EXCEPTION));
        return query.fetch().size();
    }

    // 과태료 처리 신고 건수
    @Override
    public int getSizeForPenalty(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.PENALTY));
        return query.fetch().size();
    }

    // 대기중인 신고 건수
    @Override
    public int getSizeForCOMPLETE(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.COMPLETE));
        return query.fetch().size();
    }

    @Override
    public int getSizeForPenalty(IllegalZone illegalZone) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.eq(illegalZone));
        query.where(QReport.report.reportStateType.eq(ReportStateType.PENALTY));
        return query.fetch().size();
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
