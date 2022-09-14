package com.teraenergy.illegalparking.controller.pm;

import com.teraenergy.illegalparking.controller.ExtendsController;
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
public class PmController extends ExtendsController {

    private String subTitle = "전동킥보드";

    @RequestMapping("/pm")
    public RedirectView pm(){
        return new RedirectView("/pm/map");
    }

    @RequestMapping("/pm/map")
    public ModelAndView map(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/map"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }



}
