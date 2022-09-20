package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingOrderColumn;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    public ModelAndView parkingList(HttpServletRequest request) throws Exception{

        HashMap<String, String> param = _getParam(request);

        String pageNumberStr = param.get("pageNumber");
        int pageNumber = 1;
        if ( pageNumberStr != null ) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }

        String orderColumnStr = param.get("orderColumn");
        ParkingOrderColumn orderColumn;
        if(orderColumnStr == null) {
            orderColumn = ParkingOrderColumn.parkingSeq;
        } else  {
            orderColumn = ParkingOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = param.get("filterColumn");
        ParkingFilterColumn filterColumn;
        if(filterColumnStr == null) {
            filterColumn = ParkingFilterColumn.parkingchrgeInfo;
        } else  {
            filterColumn = ParkingFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = param.get("search");
        if (searchStr == null) {
            searchStr = "";
        }

        String orderDirectionStr = param.get("orderDirection");
        Sort.Direction direction;
        if ( orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }

        String pageSizeStr = param.get("pageSize");
        int pageSize = 10;
        if ( pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
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

        int begin = pageNumber;

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("search", searchStr);

        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("pageSize", pageSizeStr);
        modelAndView.addObject("isBeginOver", isBeginOver);
        modelAndView.addObject("isEndOver", isEndOver);


        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        modelAndView.addObject("parkings", pages.getContent());

        modelAndView.setViewName(getPath("/parkingList"));

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

    private HashMap<String, String> _getParam(HttpServletRequest request) {
        HashMap<String, String> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, request.getParameter(name));
        }
        return parameterMap;
    }

}
