package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReceiptDtoServiceImpl implements ReceiptDtoService{

    @Override
    public ReceiptDto get(Receipt receipt) {
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setReceiptSeq(receipt.getReceiptSeq());
        receiptDto.setAddr(receipt.getAddr());
        receiptDto.setNote(receipt.getNote());
        receiptDto.setFileName(receipt.getFileName());
        receiptDto.setState(receipt.getStateType().getValue());
        receiptDto.setRegDt(receipt.getRegDt());
        receiptDto.setCarNum(receipt.getCarNum());
        return receiptDto;
    }

    @Override
    public List<ReceiptDto> gets(List<Receipt> receipts) {
        List<ReceiptDto> receiptDtos = Lists.newArrayList();
        for(Receipt receipt : receipts) {
            receiptDtos.add(get(receipt));
        }
        return receiptDtos;
    }
}
