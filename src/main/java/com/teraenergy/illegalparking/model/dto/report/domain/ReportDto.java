package com.teraenergy.illegalparking.model.dto.report.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.IllegalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Date : 2022-09-28
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
public class ReportDto {

    @JsonProperty
    String firstFileName;
    @JsonProperty
    String firstAddr;
    @JsonProperty
    String firstCarNum;
    @JsonProperty
    String firstRegDt;
    @JsonProperty
    IllegalType firstIllegalType;

    @JsonProperty
    String secondFileName;
    @JsonProperty
    String secondAddr;
    @JsonProperty
    String secondCarNum;
    @JsonProperty
    String secondRegDt;
    @JsonProperty
    IllegalType secondIllegalType;

    @JsonProperty
    Integer result;

    @JsonProperty
    String note;

}
