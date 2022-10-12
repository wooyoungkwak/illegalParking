package com.teraenergy.illegalparking.model.dto.user.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.domain.UserGovernmentDto;
import com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-11
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class UserGovernmentDtoServiceImpl implements UserGovernmentDtoService{

    private final UserService userService;

    private final ReportService reportService;

    @Override
    public Page<UserGovernmentDto> gets(int pageNumber, int pageSize, UserGovernmentFilterColumn userGovernmentFilterColumn, String search) throws TeraException {

        Page<User> userPages = userService.getsByGovernmentRole(pageNumber, pageSize, userGovernmentFilterColumn, search);

        List<UserGovernmentDto> userGovernmentDtos = Lists.newArrayList();

        for (User user : userPages.getContent()) {
            UserGovernmentDto userGovernmentDto = new UserGovernmentDto();
            userGovernmentDto.setUserSeq(user.getUserSeq());
            userGovernmentDto.setUserName(user.getUsername());
            userGovernmentDto.setLocationType(user.getGovernMentOffice().getLocationType().getValue());
            userGovernmentDto.setOfficeName(user.getGovernMentOffice().getName());

            // TODO : 그룹 관리 개수는 어디서 ????
            userGovernmentDto.setGroupCount(0);

            userGovernmentDto.setTotalCount(reportService.getSizeForReport(user.getUserSeq()));
            // 미처리 건수 ( )
            userGovernmentDto.setExceptionCount(reportService.getSizeForException(user.getUserSeq()));
            // 처리 건수 (과태료)
            userGovernmentDto.setPenaltyCount(reportService.getSizeForPenalty(user.getUserSeq()));
            // 대기 (지역 기준)
            userGovernmentDto.setCompleteCount(reportService.getSizeForCOMPLETE(user.getGovernMentOffice().getLocationType()));

            userGovernmentDtos.add(userGovernmentDto);
        }

        int total = (int) userPages.getTotalElements();
        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<UserGovernmentDto> page = new PageImpl<UserGovernmentDto>(userGovernmentDtos, pageRequest, total);
        return page;
    }


}
