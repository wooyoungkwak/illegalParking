package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.PointDto;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-10
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class PointDtoServiceImipl implements PointDtoService{

    private final PointService pointService;

    @Override
    public List<PointDto> gets(Integer groupSeq) {
        List<PointDto> pointDtos = Lists.newArrayList();
        List<Point> points = pointService.getsAllInGroup(groupSeq);
        for (Point point : points ) {
            pointDtos.add(get(point));
        }
        return pointDtos;
    }

    @Override
    public PointDto get(Point point) {
        PointDto pointDto = new PointDto();
        pointDto.setPointSeq(point.getPointSeq());
        pointDto.setValue(point.getValue().toString());
        pointDto.setPointType(point.getPointType().getValue());
        pointDto.setStartDate(StringUtil.convertDateToString(point.getStartDate(), "yyyy-MM-dd"));
        pointDto.setStopDate(StringUtil.convertDateToString(point.getStopDate(), "yyyy-MM-dd"));
        pointDto.setLimitValue( point.getLimitValue() == null ? "-" : point.getLimitValue().toString());
        pointDto.setResidualValue(point.getResidualValue() == null ? "-" : point.getResidualValue().toString());
        pointDto.setUseValue(point.getUseValue() == null ? "-" : point.getUseValue().toString());
        pointDto.setPointLimit(point.getIsPointLimit().booleanValue());
        pointDto.setTimeLimit(point.getIsTimeLimit().booleanValue());

        if ( point.getNote() != null && point.getNote().trim().length() > 0) {
            pointDto.setFinish(point.getNote());
        } else {
            if (point.getIsPointLimit() && point.getIsTimeLimit()) {
                pointDto.setFinish("-");
            } else if (point.getIsTimeLimit()) {
                pointDto.setFinish("?????? ??? ?????? ??????");
            } else if (point.getIsPointLimit()) {
                pointDto.setFinish("????????? ????????? ??????");
            } else {
                pointDto.setFinish("?????? ??? ?????? ?????? / ????????? ????????? ??????");
            }
        }
        return pointDto;
    }

    @Override
    public PointDto getByPointSeq(Integer pointSeq) {
        Point point = pointService.get(pointSeq);
        return get(point);
    }
}
