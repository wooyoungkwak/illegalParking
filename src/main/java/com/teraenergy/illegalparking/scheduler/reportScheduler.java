package com.teraenergy.illegalparking.scheduler;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class reportScheduler {

    private final ReceiptService receiptService;

    /**
     * OCCUR(신고발생) -> FORGET(신고누락)
     * 11분 전 부터 1시간 11분 전까지의 신고 발생 후 누락이 된 경우 모두 업데이트
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateTask() {
        LocalDateTime now = LocalDateTime.now();        // 현재 시간
        LocalDateTime startTime = now.minusMinutes(11);   // 11분 전 시간
        LocalDateTime endTime = startTime.minusMinutes(60);   // 11분 전의 60분전 시간
        List<Receipt> receipts = receiptService.gets(startTime, endTime, ReceiptStateType.OCCUR);

        if ( receipts.size() > 0) {
            for (Receipt receipt : receipts) {
                receipt.setReceiptStateType(ReceiptStateType.FORGET);
            }

            receiptService.sets(receipts);
        }

        log.info("updateTask running .... {} : {}", startTime, endTime);
    }

}
