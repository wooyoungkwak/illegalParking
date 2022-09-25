package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReceiptService {

    public Receipt get(Integer receiptSeq);

    public List<Receipt> gets();

    public Receipt set(Receipt receipt);

    public List<Receipt> sets(List<Receipt> receipts);

    public Receipt modify(Receipt receipt);

    public long remove(Integer receiptSeq);

    public long removes(List<Integer> receiptSeqs);

}
