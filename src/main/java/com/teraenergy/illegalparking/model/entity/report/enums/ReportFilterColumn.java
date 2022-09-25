
package com.teraenergy.illegalparking.model.entity.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@AllArgsConstructor
@Getter
public enum ReportFilterColumn {
    iscomplete("처리결과"),
    addr("장소"),
    illegalType("위반종류"),
    carNum("차량번호")
    ;

    private String value;

}
