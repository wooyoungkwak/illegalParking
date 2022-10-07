package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalGroup;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalGroupServcie {

    IllegalGroup get(Integer groupSeq);

    IllegalGroup set(IllegalGroup illegalGroup);
}
