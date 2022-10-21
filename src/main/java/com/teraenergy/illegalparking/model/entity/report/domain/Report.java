package com.teraenergy.illegalparking.model.entity.report.domain;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
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
    @JoinColumn(name = "ReceiptSeq")
    Receipt receipt;

    @Column
    Integer reportUserSeq;

    @Column (nullable = false)
    LocalDateTime regDt = LocalDateTime.now();

    @Column
    @Enumerated (EnumType.STRING)
    ReportStateType reportStateType;

    @Column
    String note;

    @Column (nullable = false)
    Boolean isDel = false;

    @Column
    LocalDateTime delDt;

}
