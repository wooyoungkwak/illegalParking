package com.teraenergy.illegalparking.model.mapper.illegalzone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IllegalZone {
    Integer zoneSeq;    // 불법 주정차 구역 키
    String name;        // 불법 주정차 이름
    String polygon;     // 불법 주정차 구역
    Boolean isDel;      // 삭제 여부
    String StartTime;    // 탄력 주차 가능 시작 시간
    String EndTime;      // 탄력 주차 가능 종료 시간
    Integer typeSeq;    // 불법 주정차 타입 키
    Double code;    // 법정동 키
}
