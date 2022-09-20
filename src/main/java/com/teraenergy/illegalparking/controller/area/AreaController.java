package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.controller.ExtendsController;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-03-02
 * Author : young
 * Project : sbAdmin
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class AreaController extends ExtendsController {

    private final ObjectMapper objectMapper;

    private String subTitle = "불법주정차 구역";

    @GetMapping("/area")
    public RedirectView area(){
        return new RedirectView("/area/map");
    }

    @GetMapping("/area/map")
    public ModelAndView map(HttpServletRequest request, Device device) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);

        if ( device.isMobile() || device.isTablet()) {
            modelAndView.setViewName(getMobilePath("/map"));
        } else {
            modelAndView.setViewName(getPath("/map"));
        }
        return modelAndView;
    }

    @GetMapping("/area/mapSet")
    public ModelAndView mapSet() throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        modelAndView.setViewName(getPath("/mapSet"));
        return modelAndView;
    }

}
