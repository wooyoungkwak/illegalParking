package com.teraenergy.illegalparking.controller.home;

import com.teraenergy.illegalparking.controller.ExtendsController;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
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
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/home"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

}
