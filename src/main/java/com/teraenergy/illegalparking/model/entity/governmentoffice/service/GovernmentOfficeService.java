package com.teraenergy.illegalparking.model.entity.governmentoffice.service;

import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface GovernmentOfficeService {

    GovernmentOffice get(Integer officeSeq);

    List<GovernmentOffice> gets();

    GovernmentOffice set(GovernmentOffice governmentOffice);
}
