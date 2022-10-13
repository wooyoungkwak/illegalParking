package com.teraenergy.illegalparking.controller.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.service.UserGovernmentDtoService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

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

    @PostMapping(value = "/user/userGroup/group/get")
    @ResponseBody
    public Object getGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        return userGovernmentDtoService.gets(jsonNode.get("userSeq").asInt());
    }

    @PostMapping(value = "/user/userGroup/group/name/get")
    @ResponseBody
    public Object getName(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        return illegalGroupServcie.gets(locationType);
    }

    @PostMapping("/user/userGroup/set")
    @ResponseBody
    public Object setUserGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        LocationType locationType = LocationType.valueOf(jsonNode.get("locationType").asText());
        String officeName = jsonNode.get("name").asText();
        IllegalGroup illegalGroup = illegalGroupServcie.get(locationType, officeName);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserSeq(userSeq);
        userGroup.setUserGroupSeq(illegalGroup.getGroupSeq());
        userGroup = userGroupService.set(userGroup);

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("userGroupSeq", userGroup.getGroupSeq());
        result.put("groupName", officeName);
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
