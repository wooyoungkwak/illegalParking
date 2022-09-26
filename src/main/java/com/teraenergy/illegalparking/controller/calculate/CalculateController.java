package com.teraenergy.illegalparking.controller.calculate;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.entity.calcurate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calcurate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.calcurate.service.CalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class CalculateController extends ExtendsController {

    private final CalculateService calculateService;

    private String subTitle = "결재";
    
    @RequestMapping("/calculate")
    public RedirectView calculate() {
        return new RedirectView("/calculate/calculateList");
    }

    @GetMapping("/calculate/calculateList")
    public ModelAndView calculateList(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HashMap<String, String> param = _getParam(request);

        String pageNumberStr = param.get("pageNumber");
        int pageNumber = 1;
        if ( pageNumberStr != null ) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }

        String orderColumnStr = param.get("orderColumn");
        CalculateOrderColumn orderColumn;
        if(orderColumnStr == null) {
            orderColumn = CalculateOrderColumn.calculateSeq;
        } else  {
            orderColumn = CalculateOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = param.get("filterColumn");
        CalculateFilterColumn filterColumn;
        if(filterColumnStr == null) {
            filterColumn = CalculateFilterColumn.user;
        } else  {
            filterColumn = CalculateFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = param.get("searchStr");
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

        Page<Calculate> pages = calculateService.gets(pageNumber, pageSize, filterColumn, searchStr, orderColumn, direction);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && ( totalPages - pageNumber ) > 2 ) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("filterColumn", filterColumnStr);
        modelAndView.addObject("searchStr", searchStr);
        modelAndView.addObject("orderColumn", orderColumnStr);
        modelAndView.addObject("orderDirection", orderDirectionStr);

        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("isBeginOver", isBeginOver);
        modelAndView.addObject("isEndOver", isEndOver);
        modelAndView.addObject("parkings", pages.getContent());


        modelAndView.setViewName(getPath("/calculateList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

    @GetMapping("/calculate/productList")
    public ModelAndView productList() {
        ModelAndView modelAndView = new ModelAndView();




        modelAndView.setViewName(getPath("/productList"));
        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

    @GetMapping("/calculate/productAdd")
    public ModelAndView productAdd() {
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.setViewName(getPath("/productAdd"));
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
