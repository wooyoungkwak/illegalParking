package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.receipt.domain.QReceipt;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final JPAQueryFactory jpaQueryFactory;

    private final ReceiptRepository receiptRepository;
    @Override
    public Receipt get(Integer receiptSeq) {
        return receiptRepository.findByReceiptSeqAndIsDel(receiptSeq, false);
    }

    @Override
    public List<Receipt> gets() {
        return receiptRepository.findAllByIsDel(false);
    }

    @Override
    public Receipt set(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> sets(List<Receipt> receipts) {
        return receiptRepository.saveAll(receipts);
    }

    @Override
    public Receipt modify(Receipt receipt) {
        return set(receipt);
    }

    @Override
    public long remove(Integer receiptSeq) {
        JPAUpdateClause query = jpaQueryFactory.update(QReceipt.receipt);
        query.set(QReceipt.receipt.isDel, true);
        query.where(QReceipt.receipt.receiptSeq.eq(receiptSeq));
        return query.execute();
    }

    @Override
    public long removes(List<Integer> receiptSeqs) {
        JPAUpdateClause query = jpaQueryFactory.update(QReceipt.receipt);
        query.set(QReceipt.receipt.isDel, true);
        query.where(QReceipt.receipt.receiptSeq.in(receiptSeqs));
        return query.execute();
    }
}
