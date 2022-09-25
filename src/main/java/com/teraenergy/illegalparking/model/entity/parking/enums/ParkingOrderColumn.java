package com.teraenergy.illegalparking.model.entity.parking.enums;

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
public enum ParkingOrderColumn {
    parkingSeq("순번"),
    prkplceNm("주차장명"),
    parkingchrgeInfo("요금")
    ;

    private String value;

}
