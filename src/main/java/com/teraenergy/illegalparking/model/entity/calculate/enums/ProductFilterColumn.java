package com.teraenergy.illegalparking.model.entity.calculate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-19
 * Author : young
 * Project : illegalParking
 * Description :
 */

@AllArgsConstructor
@Getter
public enum ProductFilterColumn {
    brand("브랜드"),
    name("제품명"),
    point("포인트")
    ;

    private String value;

}
