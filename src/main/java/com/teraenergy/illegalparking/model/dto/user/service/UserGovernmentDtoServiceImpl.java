package com.teraenergy.illegalparking.model.dto.user.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.domain.UserGovernmentDto;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
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
    public List<UserGovernmentDto> gets() throws TeraException {
        List<User> users = userService.getsByGovernmentRole();
        List<UserGovernmentDto> userGovernmentDtos = Lists.newArrayList();

        for (User user : users) {
            UserGovernmentDto userGovernmentDto = new UserGovernmentDto();
            userGovernmentDto.setUserSeq(user.getUserSeq());
            userGovernmentDto.setUserName(user.getUsername());
            userGovernmentDto.setLocationType(user.getGovernMentOffice().getLocationType().getValue());
            userGovernmentDto.setOfficeName(user.getGovernMentOffice().getName());

            // TODO : 그룹 관리 개수는 어디서 ????
            userGovernmentDto.setGroupCount(0);

            userGovernmentDto.setTotalCount(reportService.getSizeForReport(user.getUserSeq()));
            userGovernmentDto.setExceptionCount(reportService.getSizeForException(user.getUserSeq()));
            userGovernmentDto.setPenaltyCount(reportService.getSizeForPenalty(user.getUserSeq()));
            userGovernmentDto.setCompleteCount(reportService.getSizeForCOMPLETE(user.getUserSeq()));

            userGovernmentDtos.add(userGovernmentDto);
        }
        return userGovernmentDtos;
    }


}
