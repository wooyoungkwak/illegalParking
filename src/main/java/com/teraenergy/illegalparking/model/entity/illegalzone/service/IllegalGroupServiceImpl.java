package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalGroupServiceImpl implements IllegalGroupServcie{

    private final IllegalGroupRepository illegalGroupRepository;

    @Override
    public IllegalGroup get(Integer groupSeq) {
        Optional<IllegalGroup> optional = illegalGroupRepository.findById(groupSeq);
        if (optional.isEmpty()) return null;
        return optional.get();
    }

    @Override
    public IllegalGroup set(IllegalGroup illegalGroup) {
        return illegalGroupRepository.save(illegalGroup);
    }

}
