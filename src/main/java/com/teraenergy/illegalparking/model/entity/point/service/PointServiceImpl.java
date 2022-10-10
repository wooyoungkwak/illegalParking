package com.teraenergy.illegalparking.model.entity.point.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.domain.QPoint;
import com.teraenergy.illegalparking.model.entity.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    private final JPAQueryFactory jpaQueryFactory;

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
    public Point getInGroup(Integer groupSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QPoint.point);
        query.where(QPoint.point.GroupSeq.eq(groupSeq));
        query.orderBy(QPoint.point.pointSeq.desc());
        query.limit(1);
        return (Point) query.fetchOne();
    }

    @Override
    public List<Point> getsInGroup(Integer groupSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QPoint.point);
        query.where(QPoint.point.GroupSeq.eq(groupSeq));
        query.orderBy(QPoint.point.pointSeq.desc());
        return query.fetch();
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
