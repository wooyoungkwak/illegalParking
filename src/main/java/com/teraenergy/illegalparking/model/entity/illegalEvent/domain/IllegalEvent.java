package com.teraenergy.illegalparking.model.entity.illegalEvent.domain;

import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.IllegalType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Setter
@Getter
@Entity(name = "illegal_event")
public class IllegalEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer eventSeq;

    @Column
    String name;        // 불법 주정차 구역 이름

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    IllegalType illegalType;

    @Column
    String firstStartTime;

    @Column
    String firstEndTime;

    @Column
    Boolean usedFirst;

    @Column
    String secondStartTime;

    @Column
    String secondEndTime;

    @Column
    Boolean usedSecond;

    @Column
    @Enumerated(EnumType.STRING)
    ZoneGroupType zoneGroupType;

}
