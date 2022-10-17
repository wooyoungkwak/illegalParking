package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReceiptService {

    Receipt get(Integer receiptSeq);

    boolean isExist(Integer userSeq, String carNum, LocalDateTime regDt, String code);

    List<Receipt> gets();

    List<Receipt> gets(Integer userSeq);

    int getsOverlabCount(Integer user, LocalDateTime regDt);

    List<Receipt> gets(LocalDateTime now, LocalDateTime old, ReceiptStateType receiptStateType);

    Page<Receipt> gets(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search);


    Receipt set(Receipt receipt);

    List<Receipt> sets(List<Receipt> receipts);

    Receipt modify(Receipt receipt);

    long remove(Integer receiptSeq);

    long removes(List<Integer> receiptSeqs);

    Receipt getByCarNumAndBetweenNow(Integer userSeq, String carNum, LocalDateTime regDt);

}
