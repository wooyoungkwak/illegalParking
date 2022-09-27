package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;
import com.teraenergy.illegalparking.model.entity.calculate.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;

    @Override
    public Point get(Integer pointSeq) {
        Optional<Point> optional = pointRepository.findById(pointSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Point> gets() {
        return pointRepository.findAll();
    }

    @Override
    public Point set(Point point) {
        return pointRepository.save(point);
    }

    @Override
    public List<Point> sets(List<Point> points) {
        return pointRepository.saveAll(points);
    }

}
