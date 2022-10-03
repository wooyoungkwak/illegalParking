package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReceiptService {

    Receipt get(Integer receiptSeq);

    List<Receipt> gets();

    List<Receipt> gets(LocalDateTime now, LocalDateTime old, ReceiptType receiptType);

    Receipt set(Receipt receipt);

    List<Receipt> sets(List<Receipt> receipts);

    Receipt modify(Receipt receipt);

    long remove(Integer receiptSeq);

    long removes(List<Integer> receiptSeqs);

}
