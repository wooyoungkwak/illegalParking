package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDetailDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDetailDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    private final ReportService reportService;

    private final ReceiptService receiptService;

    private final UserService userService;

    private final CommentService commentService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final PointService pointService;

    @Override
    public ReportDto get(Report report) {
        ReportDto reportDto = new ReportDto();
        Receipt firstReceipt = report.getFirstReceipt();
        Receipt secondReceipt = report.getSecondReceipt();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

        return reportDto;
    }

    @Override
    public List<ReportDto> gets(List<Report> reports) {
        return null;
    }

    @Override
    public Page<ReceiptDto> getsFromReceipt(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search) {
        Page<Receipt> receiptPage = receiptService.gets(pageNumber, pageSize, receiptStateType, filterColumn, search);

        List<ReceiptDto> receiptDtos = Lists.newArrayList();
        for ( Receipt receipt : receiptPage.getContent() ) {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setReceiptSeq(receipt.getReceiptSeq());
            receiptDto.setAddr(receipt.getAddr());
            receiptDto.setCarNum(receipt.getCarNum());
            receiptDto.setName(receipt.getUser().getName());
            receiptDto.setOverlapCount(receiptService.getsOverlabCount(receipt.getUser().getUserSeq(), receipt.getRegDt()));
            receiptDto.setRegDt(receipt.getRegDt());
            receiptDto.setReceiptStateType(receipt.getReceiptStateType());

            receiptDtos.add(receiptDto);
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ReceiptDto> page = new PageImpl<ReceiptDto>(receiptDtos, pageRequest, receiptDtos.size());
        return page;
    }


    @Override
    public ReceiptDetailDto getFromReceiptDetailDto(Integer receiptSeq) throws TeraException {

        Receipt receipt = receiptService.get(receiptSeq);
        ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto();
        receiptDetailDto.setReceiptSeq(receipt.getReceiptSeq());
        receiptDetailDto.setName(receipt.getUser().getName());
        receiptDetailDto.setCarNum(receipt.getCarNum());
        receiptDetailDto.setOverlapCount(0); // TODO : 중복 횟수
        receiptDetailDto.setRegDt(receipt.getRegDt());
        receiptDetailDto.setReceiptStateType(receipt.getReceiptStateType());

        List<String> comments = Lists.newArrayList();
        List<Integer> receiptSeqs = Lists.newArrayList();

        receiptDetailDto.setFirstFileName(receipt.getFileName());
        receiptDetailDto.setFirstRegDt(receipt.getRegDt());
        receiptDetailDto.setFirstAddr(receipt.getAddr());
        receiptSeqs.add(receipt.getReceiptSeq());

        List<Comment> receiptComments = commentService.gets(receiptSeqs);
        for (Comment receiptComment : receiptComments) {
            comments.add(receiptComment.getContent());
        }

        receiptDetailDto.setComments(comments);
        return receiptDetailDto;
    }

    @Override
    public Page<ReportDto> getsFromReport(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search) throws TeraException {
        Page<Report> reportPage = reportService.gets(pageNumber, pageSize, reportStateType, filterColumn, search);

        List<ReportDto> reportDtos = Lists.newArrayList();
        for ( Report report : reportPage.getContent() ) {
            ReportDto reportDto = new ReportDto();
            reportDto.setReportSeq(report.getReportSeq());
            reportDto.setAddr(report.getSecondReceipt().getAddr());
            reportDto.setCarNum(report.getSecondReceipt().getCarNum());
            reportDto.setName(report.getSecondReceipt().getUser().getName());
            reportDto.setRegDt(report.getRegDt());
            reportDto.setReportStateType(report.getReportStateType());

            if ( report.getReportUserSeq() != null) {
                User user = userService.get(report.getReportUserSeq());
                reportDto.setGovernmentName(user.getGovernMentOffice().getName());

                Point point= pointService.get(report.getSecondReceipt().getIllegalZone().getIllegalEvent().getGroupSeq());
                String mark = "-";
                switch (point.getPointType()) {
                    case PLUS:
                        mark = "+";
                        break;
                    case MINUS:
                        mark = "-";
                        break;
                }
                reportDto.setEvent(mark + point.getValue().toString());
            }
            reportDtos.add(reportDto);
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ReportDto> page = new PageImpl<ReportDto>(reportDtos, pageRequest, reportDtos.size());
        return page;
    }



    @Override
    public ReportDetailDto getFromReportDetailDto(int reportSeq) throws TeraException {
        Report report = reportService.get(reportSeq);

        ReportDetailDto reportDetailDto = new ReportDetailDto();

        reportDetailDto.setReportSeq(report.getReportSeq());
        reportDetailDto.setName(report.getSecondReceipt().getUser().getName());
        reportDetailDto.setCarNum(report.getSecondReceipt().getCarNum());
        reportDetailDto.setOverlapCount(0); // TODO : 중복 횟수
        reportDetailDto.setRegDt(report.getRegDt());
        reportDetailDto.setReportStateType(report.getReportStateType()); ;
        if ( report.getReportUserSeq() != null) {
            User governmentUser = userService.get(report.getReportUserSeq());
            reportDetailDto.setGovernmentOfficeName(governmentUser.getGovernMentOffice().getName());
        }
        reportDetailDto.setNote(report.getNote());

        Receipt firstReceipt = report.getFirstReceipt();
        Receipt secondReceipt = report.getSecondReceipt();

        List<String> comments = Lists.newArrayList();
        List<Integer> receiptSeqs = Lists.newArrayList();

        if (firstReceipt != null) {
            reportDetailDto.setFirstFileName(firstReceipt.getFileName());
            reportDetailDto.setFirstRegDt(firstReceipt.getRegDt());
            reportDetailDto.setFirstAddr(firstReceipt.getAddr());
            receiptSeqs.add(firstReceipt.getReceiptSeq());
        }

        if (secondReceipt != null) {
            reportDetailDto.setSecondFileName(secondReceipt.getFileName());
            reportDetailDto.setSecondRegDt(secondReceipt.getRegDt());
            reportDetailDto.setSecondAddr(secondReceipt.getAddr());
            receiptSeqs.add(secondReceipt.getReceiptSeq());
        }

        List<Comment> receiptComments = commentService.gets(receiptSeqs);
        for (Comment receiptComment : receiptComments) {
            comments.add(receiptComment.getContent());
        }

        reportDetailDto.setComments(comments);
        return reportDetailDto;
    }
}
