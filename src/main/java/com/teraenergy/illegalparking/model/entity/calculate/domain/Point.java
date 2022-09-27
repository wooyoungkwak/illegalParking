package com.teraenergy.illegalparking.model.entity.calculate.domain;

import com.teraenergy.illegalparking.model.entity.calculate.enums.PointState;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer pointSeq;

    @Column
    String note;

    @Column
    Long value;

    @Column
    Integer userSeq;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "reportSeq")
    Report report;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "productSeq")
    Product product;

    @Column (nullable = false)
    PointState pointState;

}