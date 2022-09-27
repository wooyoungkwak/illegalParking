package com.teraenergy.illegalparking.model.entity.calculate.domain;

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
@Entity(name = "calculate")
public class Calculate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer calculateSeq;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "pointSeq")
    Point point;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "userSeq")
    User user;

    @Column
    Long currentPointValue;

    @Column
    Long beforePointValue;

    @Column (nullable = false)
    Boolean isDel;

    @Column
    LocalDateTime regDt;

}
