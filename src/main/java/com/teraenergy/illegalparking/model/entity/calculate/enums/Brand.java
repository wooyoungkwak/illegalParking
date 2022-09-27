package com.teraenergy.illegalparking.model.entity.calculate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@AllArgsConstructor
@Getter
public enum Brand {

    STARBUGS("스타벅스"),

    BASKINROBBINS("베스킨라빈스")
    ;

    private String value;
}
