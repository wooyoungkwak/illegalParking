package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalType.domain.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalType.service.IllegalTypeService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Date : 2022-09-16
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "home")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
public class SqlIllegalType {

    @Autowired
    IllegalTypeService illegalTypeService;

    @Test
    public void insert(){

        List<IllegalType> illegalTypes = Lists.newArrayList();
        
        IllegalType illegalType = new IllegalType();
        illegalType.setName("불법주정차");
        illegalType.setIsDel(false);
        
        illegalTypes.add(illegalType);

        illegalType = new IllegalType();
        illegalType.setName("5분주정차");
        illegalType.setIsDel(false);

        illegalTypes.add(illegalType);

        illegalType = new IllegalType();
        illegalType.setName("탄력주정차");
        illegalType.setIsDel(false);

        illegalTypes.add(illegalType);

        illegalTypeService.saves(illegalTypes);
    }


}

