package com.teraenergy.illegalparking.model.entity.report.enums;

import lombok.Getter;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum ResultType {
    WAIT("대기"),
    EXCEPTION("신고제외"),
    PENALTY("과태료대상")
        ;

    private String value;

    ResultType(String value) {
        this.value = value;
    }

}
