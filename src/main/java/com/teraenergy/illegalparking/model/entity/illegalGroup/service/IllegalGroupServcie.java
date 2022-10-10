package com.teraenergy.illegalparking.model.entity.illegalGroup.service;

import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn;
import org.springframework.data.domain.Page;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalGroupServcie {

    IllegalGroup get(Integer groupSeq);

    Page<IllegalGroup> get(Integer pageNumber, Integer pageSize, GroupFilterColumn filterColumn, String search);

    IllegalGroup set(IllegalGroup illegalGroup);
}
