package com.teraenergy.illegalparking.controller.home;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class HomeController extends ExtendsController {

    private final UserService userService;
    private String subTitle = "불법 주정차";

    @RequestMapping("/")
    public RedirectView home_(Device device) throws TeraException {
        if (device.isMobile()) {
            return new RedirectView("/area/map");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.get(auth.getName());
        switch (user.getRole()) {
            case ADMIN:
                return new RedirectView("/notice/noticeList");
            case GOVERNMENT:
            default:
                return new RedirectView("/home");
        }
    }

    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        model.addAttribute("subTitle", subTitle);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.get(auth.getName());
        switch (user.getRole()) {
            case ADMIN:
                return "/";
            case GOVERNMENT:
            default:
                return getPath("/home");
        }
    }

}
