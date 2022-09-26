package com.teraenergy.illegalparking.model.entity.illegalEvent.domain;

import com.teraenergy.illegalparking.model.entity.environment.domain.Environment;
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
public class illegalEvent {

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

    @OneToOne(optional = false)
    @JoinColumn(name = "EnvironmentSeq")
    Environment environment;

}
