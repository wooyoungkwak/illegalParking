package com.teraenergy.illegalparking.model.entity.report.domain;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.report.enums.ResultType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Integer reportSeq;

    @OneToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "FirstReceiptSeq")
    Receipt firstReceipt;

    @OneToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "SecondReceiptSeq")
    Receipt secondReceipt;

    @Column
    Integer reportUserSeq;

    @Column
    LocalDateTime regDt;

    @Column
    @Enumerated (EnumType.STRING)
    ResultType resultType;

    @Column
    String note;

    @Column
    Integer zoneSeq;

    @Column
    String code;

    @Column
    Boolean isDel;

    @Column
    LocalDateTime delDt;

}
