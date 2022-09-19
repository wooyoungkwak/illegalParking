package com.teraenergy.illegalparking.model.entity.parking.enums;

/**
 * Date : 2022-09-19
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
public enum ParkingOrderColumn {
    parkingSeq("순번"),
    prkplceNm("주차장명"),
    parkingchrgeInfo("요금")
    ;

    private String value;

    ParkingOrderColumn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
