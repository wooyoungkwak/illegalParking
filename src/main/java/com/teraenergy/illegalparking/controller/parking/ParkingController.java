package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class ParkingController extends ExtendsController {

    private final ObjectMapper objectMapper;

    private final ParkingService parkingService;

    private String subTitle = "공영주차장";

    @GetMapping("/parking")
    public RedirectView parking(){
        return new RedirectView("/parking/map");
    }

    @GetMapping("/parking/map")
    public ModelAndView map(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/map"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

    @GetMapping("/parking/parkingList")
    public ModelAndView parkingList(HttpServletRequest request){

        HashMap<String, Object> param = _getParam(request);

        int pageNumber = 1;
        if ( param.get("pageNumber") != null ) {
            pageNumber = (int) param.get("pageNumber");
        }

        int offset = 0;
        int limit = 10;

        List<Parking> parkings = parkingService.gets();
        int totalSize = parkings.size();

        int begin = pageNumber;
        int end = 5;

        boolean isBeginOver = false;
        boolean isEndOver = false;

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rowNumber", offset);
        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("begin", begin);
        modelAndView.addObject("end", end);
        modelAndView.addObject("isBeginOver", isBeginOver);
        modelAndView.addObject("isEndOver", isEndOver);

        modelAndView.setViewName(getPath("/parkingList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        modelAndView.addObject("parkings", parkings);
        return modelAndView;
    }

    @GetMapping("/parking/parkingAdd")
    public ModelAndView parkingAdd(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/parkingAdd"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

    private HashMap<String, Object> _getParam(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, request.getParameter(name));
        }
        return parameterMap;
    }

}
