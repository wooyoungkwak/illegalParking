package com.teraenergy.illegalparking.model.entity.illegalType.service;

import com.teraenergy.illegalparking.model.entity.illegalType.domain.IllegalType;

import java.util.List;

/**
 * Date : 2022-09-16
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface IllegalTypeService {

    IllegalType get(Integer typeSeq);

    List<IllegalType> gets();

    IllegalType save(IllegalType illegalType);

    List<IllegalType> saves(List<IllegalType> illegalTypes);

}
