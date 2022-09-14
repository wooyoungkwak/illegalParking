package com.teraenergy.illegalparking.controller.calculate;

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
public class CalculateController extends ExtendsController {

    private String subTitle = "결재";
    
    @RequestMapping("/calculate")
    public RedirectView calculate() {
        return new RedirectView("/calculate/calculateList");
    }

    @RequestMapping("/calculate/calculateList")
    public ModelAndView calculateList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/calculateList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

}
