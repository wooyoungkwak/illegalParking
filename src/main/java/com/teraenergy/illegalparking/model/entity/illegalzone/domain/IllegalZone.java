package com.teraenergy.illegalparking.model.entity.illegalzone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
@Entity
public class IllegalZone {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Integer zoneSeq;    // 불법 주정차 구역 키

    @Column
    String name;        // 불법 주정차 이름

    @Column
    String polygon;     // 불법 주정차 구역

    @Column
    Boolean isDel;      // 삭제 여부

    @Column
    String StartTime;    // 탄력 주차 가능 시작 시간

    @Column
    String EndTime;      // 탄력 주차 가능 종료 시간

    @Column
    Integer typeSeq;    // 불법 주정차 타입 키

    @Column
    String code;    // 법정동 키
}
