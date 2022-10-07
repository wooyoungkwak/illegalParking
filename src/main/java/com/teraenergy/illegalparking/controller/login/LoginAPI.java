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
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
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
    public JsonNode login (@RequestBody String body) {
        HashMap<String, Object> resultMap = Maps.newHashMap();

        boolean result = false;
        UserDto userDto = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();

            result = userService.isUser(username, password);
            if (result) {
                User user = userService.get(username);
                userDto = userDtoService.get(user);
            }

        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        } catch (TeraException e) {
            log.error(e.getMessage(), e);
        } finally {
            resultMap.put("success", result);
            if (userDto != null) {
                resultMap.put("user", userDto);
                resultMap.put("msg", "");
            } else {
                resultMap.put("msg", TeraExceptionCode.USER_IS_NOT_EXIST.getMessage());
            }
            String jsonStr = null;
            try {
                jsonStr = objectMapper.writeValueAsString(resultMap);
                return objectMapper.readTree(jsonStr);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

}
