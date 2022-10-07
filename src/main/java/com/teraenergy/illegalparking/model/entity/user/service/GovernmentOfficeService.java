package com.teraenergy.illegalparking.model.entity.user.service;

import com.teraenergy.illegalparking.model.entity.user.domain.GovernmentOffice;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface GovernmentOfficeService {

    List<GovernmentOffice> gets();

    GovernmentOffice set(GovernmentOffice governmentOffice);
}
