package com.teraenergy.illegalparking.controller.home;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private String subTitle = "불법 주정차";

    @RequestMapping("/")
    public RedirectView home_(Device device) {
        if (device.isNormal()) {
            return new RedirectView("/home");
        } else {
            return new RedirectView("/area/map");
        }
    }

    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        model.addAttribute("subTitle", subTitle);
        return getPath("/home");
    }

}
