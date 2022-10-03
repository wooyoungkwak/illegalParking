package com.teraenergy.illegalparking.controller.myinfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

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
        String jsonStr = objectMapper.writeValueAsString(userDto);
        return objectMapper.readTree(jsonStr);
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
        String jsonStr = objectMapper.writeValueAsString(userDto);
        return objectMapper.readTree(jsonStr);
    }

}
