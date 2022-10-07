package com.teraenergy.illegalparking.model.dto.report.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class ReceiptDto {
    Integer receiptSeq;          // 신고 접수 키
    String addr;                // 불법주정차 신고 위치
    String note;                // 결과 내용
    String fileName;            // 파일 이름
    String state;               // 결과 상태 (receiptType - 신고대기/신고종료/신고제외/신고접수/과태료대상)
    String carNum;              // 차량 번호
    String overlap;             // 증복 건수
    LocalDateTime regDt;        // 접수 일자
}
