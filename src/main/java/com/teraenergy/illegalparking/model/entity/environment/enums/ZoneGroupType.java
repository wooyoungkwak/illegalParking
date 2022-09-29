package com.teraenergy.illegalparking.model.entity.environment.enums;

import lombok.Getter;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
public enum ZoneGroupType {

    GROUP_A(1000L),
    GROUP_B(2000L),
    GROUP_C(3000L),
    GROUP_D(4000L),
    GROUP_E(5000L);

    private Long value;

    ZoneGroupType(Long value) {
        this.value = value;
    }
}
