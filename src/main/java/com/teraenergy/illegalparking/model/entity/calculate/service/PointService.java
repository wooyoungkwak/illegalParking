package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Point;

import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface PointService {

    Point get(Integer pointSeq);

    List<Point> gets();

    Point set(Point point);

    List<Point> sets(List<Point> points);

}
