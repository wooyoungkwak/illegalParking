package com.teraenergy.illegalparking.model.entity.receipt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@AllArgsConstructor
public enum ReceiptFilterColumn {

    CAR_NAME("차량번호")
    ;

    private String value;
}
