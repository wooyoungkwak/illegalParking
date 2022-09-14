package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView parkingList(){
        ModelAndView modelAndView = new ModelAndView();
        List<Parking> parkings = parkingService.gets();

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


}
