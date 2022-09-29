package com.teraenergy.illegalparking.model.entity.illegalEvent.domain;

import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
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
    Integer EventSeq;

    @Column
    String FirstStartTime;

    @Column
    String FirstEndTime;

    @Column
    Boolean UsedFirst;

    @Column
    String SecondStartTime;

    @Column
    String SecondEndTime;

    @Column
    Boolean UsedSecond;

    @Column
    @Enumerated(EnumType.STRING)
    ZoneGroupType zoneGroupType;

}
