package com.teraenergy.illegalparking.model.entity.illegalzone.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Setter
@Getter
@Entity
public class IllegalGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Integer GroupSeq;

    @Column
    String Name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    LocationType locationType;

    @Column
    Boolean IsDel = false;

    @Column
    LocalDateTime DelDt;

}
