package com.teraenergy.illegalparking.model.entity.illegalzone.enums;

import lombok.Getter;

/**
 * Date : 2022-09-23
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum IllegalType {

    ILLEGAL("불법주정차"),
    FIVE_MINUTE("5분주정차");

    private String value;

    IllegalType(String value) {
        this.value = value;
    }

}
