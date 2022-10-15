package com.teraenergy.illegalparking.controller.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Slf4j
@Controller
public class LoginAPI {

    private final UserService userService;
    private final UserDtoService userDtoService;
    private final ObjectMapper objectMapper;

    @PostMapping("/api/login")
    @ResponseBody
    public Object login(@RequestBody String body) throws TeraException {

        boolean result = false;
        UserDto userDto = null;
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String username = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();

        result = userService.isUser(username, password);

        if (result) {
            User user = userService.get(username);
            userDto = userDtoService.get(user);
        } else {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
        }

        return userDto;
    }

    @PostMapping("/api/user/register")
    @ResponseBody
    public Object register(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();
        String name = jsonNode.get("name").asText();
        String phoneNumber = jsonNode.get("phoneNumber").asText();
        String photoName = jsonNode.get("photoName").asText();

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setName(name);
        user.setRole(Role.USER);
        user.setUserCode(1L);
        user.setPhoneNumber(phoneNumber);
        user.setPhotoName(photoName);
        user.setEncyptPassword();

        try {
            userService.set(user);
            return "";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_FAIL_RESiSTER);
        }
    }

    @PostMapping("/api/user/isExist")
    @ResponseBody
    public Object isExist(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();

        HashMap<String, Object> resultMap = Maps.newHashMap();

        try {
            if (userService.isUser(userName)) {
                resultMap.put("isExist", true);
                resultMap.put("msg", TeraExceptionCode.USER_IS_NOT_EXIST.getMessage());
            } else {
                resultMap.put("isExist", false);
            }
            return resultMap;
        } catch (TeraException e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }



}
