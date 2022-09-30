package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String parkingList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String orderColumnStr = paramMap.getAsString("orderColumn");
        ParkingOrderColumn orderColumn;
        if(orderColumnStr == null) {
            orderColumn = ParkingOrderColumn.parkingSeq;
        } else  {
            orderColumn = ParkingOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ParkingFilterColumn filterColumn;
        if(filterColumnStr == null) {
            filterColumn = ParkingFilterColumn.parkingchrgeInfo;
        } else  {
            filterColumn = ParkingFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = paramMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
        }

        String orderDirectionStr = paramMap.getAsString("orderDirection");
        Sort.Direction direction;
        if ( orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if ( pageSize == null) {
            pageSize = 10;
        }

        Page<Parking> pages = parkingService.gets(pageNumber, pageSize, filterColumn, searchStr, orderColumn, direction);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && ( totalPages - pageNumber ) > 2 ) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("parkings", pages.getContent());
        model.addAttribute("mainTitle", mainTitle);
        model.addAttribute("subTitle", subTitle);

        return getPath("/parkingList");
    }

    @GetMapping("/parking/parkingAdd")
    public String parkingAdd(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("mainTitle", mainTitle);
        model.addAttribute("subTitle", subTitle);

        return getPath("/parkingAdd");
    }

}
