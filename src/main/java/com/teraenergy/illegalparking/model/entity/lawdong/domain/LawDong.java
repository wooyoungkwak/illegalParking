package com.teraenergy.illegalparking.model.entity.lawdong.domain;

import lombok.Getter;
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
@Entity(name = "law_dong")
public class LawDong {
    @Id
    @Column (nullable = false)
    Double code;

    @Column (nullable = false)
    String name;

    @Column (nullable = false)
    Boolean isDel;
}
