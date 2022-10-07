package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.Getter;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Getter
public enum ReplyType {

    TIME_OVER("불법주정차 추가 신고 시간이 초과했습니다."),

    REPORT_COMPLETE("불법주정차 신고가 접수 완료되어 해당 부서에 전송되었습니다."),

    GIVE_PANELTY("해당 불법주정차 차량에 과태료가 부가되었습니다.")

    ;

    private String value;

    ReplyType(String value) {
        this.value = value;
    }

}
