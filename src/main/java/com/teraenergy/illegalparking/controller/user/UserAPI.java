package com.teraenergy.illegalparking.controller.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.service.UserGovernmentDtoService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping(value = "/userGroup/group/get")
    @ResponseBody
    public Object getGroup(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        return userGovernmentDtoService.gets(jsonNode.get("userSeq").asInt());
    }

}
