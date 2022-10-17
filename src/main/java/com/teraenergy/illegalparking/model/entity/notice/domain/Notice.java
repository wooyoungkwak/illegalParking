package com.teraenergy.illegalparking.model.entity.notice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Setter
@Getter
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer noticeSeq;

    @Column
    String subject;

    @Column
    String content;

    @Column
    Integer userSeq;

    @Column
    LocalDateTime regDt = LocalDateTime.now();

    @Column
    boolean isDel = false;

    @Column
    LocalDateTime delDt;

}
