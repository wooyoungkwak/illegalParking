package com.teraenergy.illegalparking.model.entity.calculate.domain;

import com.teraenergy.illegalparking.model.entity.calculate.enums.Brand;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
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
    Brand brand;

    @Column
    Long pointValue;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "userSeq")
    User user;

    @Column
    LocalDateTime regDt;

    @Column
    Boolean isDel;

}