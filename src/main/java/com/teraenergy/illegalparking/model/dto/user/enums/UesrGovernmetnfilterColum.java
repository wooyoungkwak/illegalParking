package com.teraenergy.illegalparking.model.dto.user.enums;

/**
 * Date : 2022-10-11
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public enum UesrGovernmetnfilterColum {

    LOCATION ("지역"),

    OFFICE_NAME("관공서명")
    ;

    String value;

    UesrGovernmetnfilterColum(String value) {
        this.value = value;
    }
}
