package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.Getter;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */
@Getter
public enum StateType {


    /**
     * 1. 최초 신고 상태 : 신고 발생
     * 2. 두번째 신고 상태 : 신고 접수  or 신고 누락 -> 신고 발생으로 등록
     * 3. 결과 : 과태료 대상 or 신고 제외
     *
     * 신고 발생(1), 신고 접수(2), 신고 누락(3), 신고 제외(4), 과태료 대상(5)
     * */

    OCCUR("신고발생"),
    REPORT("신고접수"),
    FORGET("신고누락"),
    EXCEPTION("신고제외"),
    PENALTY("과태료 대상")
    ;

    private String value;

    StateType(String value) {
        this.value = value;
    }

}
