package com.teraenergy.illegalparking.model.entity.calcurate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer productSeq;

    @Column
    String name;

    @Column
    String iconName;

    @Column
    Integer point;

    @Column
    Integer userSeq;

    @Column
    LocalDateTime regDt;

}
