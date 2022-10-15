package com.teraenergy.illegalparking.controller.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.service.UserGovernmentDtoService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class UserAPI {

    private final UserGovernmentDtoService userGovernmentDtoService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final UserGroupService userGroupService;

    @PostMapping(value = "/user/userGroup/gets")
    @ResponseBody
    public Object getGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);

        List<UserGroup> userGroups = userGroupService.getsByUser(jsonNode.get("userSeq").asInt());

        List<HashMap<String, Object>> userGroupDtos = Lists.newArrayList();
        for (UserGroup userGroup : userGroups) {
            HashMap<String, Object> userGroupDto = Maps.newHashMap();
            userGroupDto.put("userGroupSeq", userGroup.getUserGroupSeq());
            IllegalGroup illegalGroup = illegalGroupServcie.get(userGroup.getGroupSeq());
            userGroupDto.put("name", illegalGroup.getName());
            userGroupDtos.add(userGroupDto);
        }

        return userGroupDtos;
    }

    @PostMapping(value = "/user/userGroup/group/name/get")
    @ResponseBody
    public Object getName(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        return illegalGroupServcie.getsNameByUserGroup(locationType);
    }

    @PostMapping("/user/userGroup/set")
    @ResponseBody
    public Object setUserGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        String name = jsonNode.get("name").asText();
        IllegalGroup illegalGroup = illegalGroupServcie.get(locationType, name);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserSeq(userSeq);
        userGroup.setGroupSeq(illegalGroup.getGroupSeq());

        if ( userGroupService.isExist(userSeq, illegalGroup.getGroupSeq())) {
            throw new TeraException(TeraExceptionCode.USER_GROUP_IS_EXIST);
        }

        userGroup = userGroupService.set(userGroup);

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("userGroupSeq", userGroup.getUserGroupSeq());
        result.put("name", name);
        return result;
    }

    @PostMapping("/user/userGroup/remove")
    @ResponseBody
    public Object removeUserGroup(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userGroupSeq = jsonNode.get("userGroupSeq").asInt();
            userGroupService.remove(userGroupSeq);
            return "complete";
        } catch (TeraException e){
            throw new TeraException(e.getCode());
        }
    }

}
