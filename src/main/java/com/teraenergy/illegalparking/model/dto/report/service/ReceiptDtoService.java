package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;

import java.util.List;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ReceiptDtoService {

    ReceiptDto get(Receipt receipt);

    List<ReceiptDto> gets(List<Receipt> receipts);

}
