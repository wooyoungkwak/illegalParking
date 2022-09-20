package com.teraenergy.illegalparking.model.entity.parking.enums;

/**
 * Date : 2022-09-19
 * Author : young
 * Project : illegalParking
 * Description :
 */
public enum ParkingFilterColumn {
    prkplceNm("주차장명"),
    parkingchrgeInfo("요금")
    ;

    private String value;

    ParkingFilterColumn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
