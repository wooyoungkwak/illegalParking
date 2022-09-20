package com.teraenergy.illegalparking.controller.login;

import com.google.common.base.Strings;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.RequestUtil;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Slf4j
@Controller
public class LoginController extends ExtendsController {


    private final UserService userService;

    /* GET */

    @GetMapping(value = "/login")
    public String login(Model model, HttpServletRequest request) {
        // 인증 페이지로 이동하기 전 URL 기억
        String header = request.getHeader("home");
        request.getSession().setAttribute("prevPage", header);
        return getPath("/login");
    }

    @GetMapping("/password")
    public String password(Model model, HttpServletRequest request){
        return getPath("/password");
    }

    @GetMapping(value = "/register")
    public String register(Model model, HttpServletRequest request){
        return getPath("/register");
    }


    /* POST */

    @PostMapping(value = "/register")
    @ResponseBody
    public String register_(HttpServletRequest request, @RequestBody String body){
        try {
            User user = new User();
            // TODO : body parsing 후 user 값으로 적용 후

//            userService.add(user);

        } catch (TeraException e) {
            return "/register";
        }
        return "/login";
    }

}
