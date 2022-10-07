package com.teraenergy.illegalparking.controller.user;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class UserController extends ExtendsController {

    private final UserService userService;

    private String subTitle = "사용자";

    @GetMapping("/user/userList")
    public String user(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("subTitle", subTitle);
        return getPath("/userList");
    }



}
