package com.teraenergy.illegalparking.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneJpaService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlReceipt {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private IllegalZoneJpaService illegalZoneJpaService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void insert() throws TeraException {
        List<Receipt> receipts = Lists.newArrayList();

        IllegalZone illegalZone = illegalZoneJpaService.get(1);
        User user = userService.get(2);

        Receipt receipt1 = new Receipt();
        receipt1.setIllegalZone(illegalZone);
        receipt1.setUser(user);
        receipt1.setRegDt(LocalDateTime.now().minusMinutes(1));
        receipt1.setCarNum("123가1234");
        receipt1.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt1.setNote("테스트 ... 1차 ");
        receipt1.setCode("5013032000");
        receipt1.setReceiptType(ReceiptType.REPORT);
        receipt1.setIsDel(false);
        receipt1.setAddr("전라남도 나주시 빛가람동 상야1길 7");
        receipts.add(receipt1);


        Receipt receipt2 = new Receipt();
        receipt2.setIllegalZone(illegalZone);
        receipt2.setUser(user);
        receipt2.setRegDt(LocalDateTime.now());
        receipt2.setCarNum("123가1234");
        receipt2.setFileName("2F0D5DABDE074B3BA8BF9E82A89B3F81.jpg");
        receipt2.setNote("테스트 ... 2차 ");
        receipt2.setCode("5013032000");
        receipt2.setReceiptType(ReceiptType.PENALTY);
        receipt2.setIsDel(false);
        receipt2.setAddr("전라남도 나주시 산포면 신도리 378-4");
        receipts.add(receipt2);

        receiptService.sets(receipts);
    }

    @Test
    public void select(){


    }
}
