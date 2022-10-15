package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.receipt.domain.QReceipt;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.repository.ReceiptRepository;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
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
    public boolean isExist(Integer userSeq, String carNum, LocalDateTime regDt, String code) {
        LocalDateTime beforeRegDt = regDt.minusMinutes(11);
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.regDt.between(beforeRegDt, regDt));
        query.where(QReceipt.receipt.illegalZone.code.eq(code));

        if (query.fetch().size() > 0) {
            return true;
        }

        return false;

    }

    @Override
    public List<Receipt> gets() {
        return receiptRepository.findAllByIsDel(false);
    }

    @Override
    public List<Receipt> gets(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public int getsOverlabCount(Integer user, LocalDateTime regDt) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(user));
        query.where(QReceipt.receipt.regDt.before(regDt));
        return query.fetch().size();
    }

    @Override
    public List<Receipt> gets(LocalDateTime now, LocalDateTime old, ReceiptStateType receiptStateType) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.regDt.between(now, old));
        query.where(QReceipt.receipt.isDel.isFalse());
        query.where(QReceipt.receipt.receiptStateType.eq(receiptStateType));
        return query.fetch();
    }

    @Override
    public Page<Receipt> gets(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case CAR_NUM:
                    query.where(QReceipt.receipt.carNum.contains(search));
                    break;
                case ADDR:
                    query.where(QReceipt.receipt.addr.contains(search));
                    break;
                case USER:
                    query.where(QReceipt.receipt.user.name.contains(search));
                    break;
            }
        }

        query.where(QReceipt.receipt.isDel.isFalse());

        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.REPORT));     // 시고 접수  (처리완료)
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.PENALTY));    // 과태료대상 (처리완료)

        int total = query.fetch().size();

        if (receiptStateType != null) {
            query.where(QReceipt.receipt.receiptStateType.eq(receiptStateType));
        }

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Receipt> receipts = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Receipt> page = new PageImpl<Receipt>(receipts, pageRequest, total);
        return page;
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
