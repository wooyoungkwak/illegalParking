package com.teraenergy.illegalparking.controller.myinfo;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyInfoApi {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserDtoService userDtoService;

    @PostMapping ("/myInfo/get")
    @ResponseBody
    public JsonNode get(@RequestBody String body) throws TeraException, JsonProcessingException {
        JsonNode requestJson =  objectMapper.readTree(body);
        Integer userSeq = requestJson.get("userSeq").asInt();
        User user = userService.get(userSeq);

        UserDto userDto = userDtoService.get(user);
        return JsonUtil.toJsonNode(userDto);
    }

    @PostMapping ("/myInfo/set")
    @ResponseBody
    public JsonNode set(@RequestBody String body) throws TeraException, JsonProcessingException {
        JsonNode requestJson =  objectMapper.readTree(body);
        Integer userSeq = requestJson.get("userSeq").asInt();

        String roleStr = requestJson.get("role").asText();
        String password = requestJson.get("password").asText();
        String name = requestJson.get("name").asText();

        User user = userService.get(userSeq);
        user.setPassword(password);
        user.setEncyptPassword();
        user.setRole(Role.valueOf(roleStr));
        user.setName(name);
        user = userService.set(user);

        UserDto userDto = userDtoService.get(user);
        return JsonUtil.toJsonNode(userDto);
    }

    @PostMapping ("/api/myInfo/get")
    @ResponseBody
    public JsonNode getByApi(Device device, @RequestBody String body) {
        if ( device.isMobile()) {
            HashMap<String, Object> resultMap = Maps.newHashMap();
            boolean isSuccess = false;
            UserDto userDto = null;
            try {
                JsonNode requestJson = objectMapper.readTree(body);
                Integer userSeq = requestJson.get("userSeq").asInt();
                User user = userService.get(userSeq);
                userDto = userDtoService.get(user);
                isSuccess = true;
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (isSuccess) {
                    resultMap.put("msg", "");
                } else {
                    userDto = new UserDto();
                    resultMap.put("msg", TeraExceptionCode.USER_GET_FAIL.getMessage());
                }
                resultMap.put("success", isSuccess);
                resultMap.put("user", userDto);
                try {
                    return JsonUtil.toJsonNode(resultMap);
                } catch (TeraException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            return null;
        }
    }

    @PostMapping ("/api/myInfo/set")
    @ResponseBody
    public JsonNode setByApi(Device device, @RequestBody String body) {
        if (device.isMobile()) {
            HashMap<String, Object> resultMap = Maps.newHashMap();
            boolean isSuccess = false;
            UserDto userDto = null;
            try {
                JsonNode requestJson = objectMapper.readTree(body);
                Integer userSeq = requestJson.get("userSeq").asInt();

                String roleStr = requestJson.get("role").asText();
                String password = requestJson.get("password").asText();
                String name = requestJson.get("name").asText();

                User user = userService.get(userSeq);
                user.setPassword(password);
                user.setEncyptPassword();
                user.setRole(Role.valueOf(roleStr));
                user.setName(name);
                user = userService.set(user);
                userDto = userDtoService.get(user);
                isSuccess = true;
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            } catch (TeraException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (isSuccess) {
                    resultMap.put("msg", "");
                } else {
                    userDto = new UserDto();
                    resultMap.put("msg", TeraExceptionCode.USER_INSERT_FAIL.getMessage());
                }
                resultMap.put("success", isSuccess);
                resultMap.put("user", userDto);
                try {
                    return JsonUtil.toJsonNode(userDto);
                } catch (TeraException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            return null;
        }
    }

}
