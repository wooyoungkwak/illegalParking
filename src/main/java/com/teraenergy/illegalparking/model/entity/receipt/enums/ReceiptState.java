package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-25
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@AllArgsConstructor
@Getter
public enum ReceiptState {

    ING("접수진행중"),

    COMPLETE("접수완료")
    ;


    private String value;

}
