package com.teraenergy.illegalparking.model.entity.illegalType.service;

import com.teraenergy.illegalparking.model.entity.illegalType.domain.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalType.repository.IllegalTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-16
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@AllArgsConstructor
@Service
public class IllegalTypeServiceImpl implements IllegalTypeService {

    private final IllegalTypeRepository illegalTypeRepository;

    @Override
    public IllegalType get(Integer typeSeq) {
        return illegalTypeRepository.findById(typeSeq) == null ? null : illegalTypeRepository.findById(typeSeq).get();
    }

    @Override
    public List<IllegalType> gets() {
        return illegalTypeRepository.findAll();
    }

    @Override
    public IllegalType save(IllegalType illegalType) {
        return illegalTypeRepository.save(illegalType);
    }

    @Override
    public List<IllegalType> saves(List<IllegalType> illegalTypes) {
        return illegalTypeRepository.saveAll(illegalTypes);
    }

}
