package com.teraenergy.illegalparking.model.entity.calcurate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@AllArgsConstructor
public enum PointState {

    PLUST_STATE("추가포인트"),

    MINUS_STATE("사용포인트");

    String value;
}
