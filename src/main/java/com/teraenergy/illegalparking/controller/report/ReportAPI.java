package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReplyType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReportAPI {

    private final IllegalZoneService illegalZoneService;
    private final IllegalZoneMapperService illegalZoneMapperService;
    private final IllegalEventService illegalEventService;
    private final UserService userService;
    private final ReceiptService receiptService;
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final PointService pointService;
    private final CalculateService calculateService;
    private final ReportDtoService reportDtoService;
    private final CommentService commentService;

    // 신고 접수 정보 등록
    @PostMapping("/report/set")
    @ResponseBody
    public Object setReport(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            String note = jsonNode.get("note").asText();
            ReportStateType reportStateType = ReportStateType.valueOf(jsonNode.get("reportStateType").asText());
            Integer userSeq = jsonNode.get("userSeq").asInt();

            // 신고 접수를 판단하는 사용자 (관공서)
            User user = userService.get(userSeq);

            Report report = reportService.get(reportSeq);
            report.setNote(note);
            report.setReportStateType(reportStateType);
            report.setReportUserSeq(userSeq);

            // 신고 등록 (Receipt) 에 대한 결과 정보 변경
            Receipt receipt = report.getReceipt();

            switch (reportStateType) {
                case PENALTY:
                    receipt.setReceiptStateType(ReceiptStateType.PENALTY);

                    Integer groupSeq = receipt.getIllegalZone().getIllegalEvent().getGroupSeq();
                    List<Point> points = pointService.getsInGroup(groupSeq);

                    long pointValue = 0L;
                    Point updatePoint = null;
                    for ( Point point : points) {

                        updatePoint = point;
                        // 포인트 제한 없음
                        if (point.getIsPointLimit() == true) {
                            pointValue = point.getValue();
                            break;
                        }

                        // 시간 제한 없음
                        if ( point.getIsTimeLimit() == true) {
                            if (point.getValue() < point.getResidualValue()) {
                                continue;
                            } else {
                                pointValue = point.getValue();
                                point.setResidualValue(point.getResidualValue() - pointValue);      // 남은 포인트
                                point.setUseValue(point.getUseValue() + pointValue);                // 누적 포인트
                            }
                        } else {
                            // 제한 시간에서 포인트 체크
                            if ( point.getStartDate().isAfter(LocalDate.now()) && point.getStopDate().isBefore(LocalDate.now()) ) {
                                if (point.getValue() < point.getResidualValue()) {
                                    continue;
                                } else {
                                    pointValue = point.getValue();
                                    point.setResidualValue(point.getResidualValue() - pointValue);      // 남은 포인트
                                    point.setUseValue(point.getUseValue() + pointValue);                // 누적 포인트
                                }
                            }
                        }
                    }

                    List<Comment> commentList = Lists.newArrayList();

                    // 댓글 1
                    Comment firstComment = new Comment();
                    firstComment.setReceiptSeq(receipt.getReceiptSeq());
                    firstComment.setContent(ReplyType.REPORT_COMPLETE.getValue());

                    // 댓글 2
                    Comment secondComment = new Comment();
                    secondComment.setReceiptSeq(receipt.getReceiptSeq());
                    secondComment.setContent(ReplyType.GIVE_PENALTY.getValue());

                    // 댓글 3 ( 관공서 내용 )
                    Comment thirdComment = new Comment();
                    thirdComment.setReceiptSeq(receipt.getReceiptSeq());
                    thirdComment.setContent(note);

                    // 댓글 4
                    Comment forthComment = new Comment();
                    forthComment.setReceiptSeq(receipt.getReceiptSeq());
                    String pointContent = "";
                    if (updatePoint != null) {
                        pointService.set(updatePoint);
                        pointContent = user.getGovernMentOffice().getLocationType().getValue();
                        pointContent += ( "로 부터 포상금 " + updatePoint.getValue()  );
                        pointContent += "포인트 제공되었습니다.";

                    } else {
                        pointContent = "포인트가 모두 소진되어 제공이 불가합니다.";
                    }
                    forthComment.setContent(pointContent);


                    commentList.add(firstComment);
                    commentList.add(secondComment);
                    commentList.add(thirdComment);
                    commentList.add(forthComment);

                    commentService.sets(commentList);
                    break;
                case EXCEPTION:
                    Comment comment = new Comment();
                    receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);
                    comment.setContent(ReplyType.REPORT_EXCEPTION.getValue());
                    comment.setReceiptSeq(receipt.getReceiptSeq());
                    commentService.set(comment);
                    break;
            }

            report.setReceipt(receipt);
            reportService.set(report);

            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.REPORT_REGISTER_FAIL);
        }
    }

    // 신고 접수 정보 가져오기
    @PostMapping("/report/get")
    @ResponseBody
    public Object getReport(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer reportSeq = jsonNode.get("reportSeq").asInt();
        return reportDtoService.getFromReportDetailDto(reportSeq);
    }

    // 신고 등록 정보 가져오기
    @PostMapping("/report/receipt/get")
    @ResponseBody
    public Object getReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer receiptSeq = jsonNode.get("receiptSeq").asInt();
        return reportDtoService.getFromReceiptDetailDto(receiptSeq);
    }

}
