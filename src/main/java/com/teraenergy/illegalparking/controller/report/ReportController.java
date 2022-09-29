package com.teraenergy.illegalparking.controller.report;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportOrderColumn;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class ReportController extends ExtendsController {


    private final ReportService reportService;

    private String subTitle = "신고";

    @GetMapping(value = "/report")
    public RedirectView report() {
        return new RedirectView("/report/reportList");
    }

    @GetMapping(value = "/report/reportList")
    public ModelAndView reportList(HttpServletRequest request) {

        HashMap<String, String> param = _getParam(request);

        String pageNumberStr = param.get("pageNumber");
        int pageNumber = 1;
        if (pageNumberStr != null) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }

        String orderColumnStr = param.get("orderColumn");
        ReportOrderColumn orderColumn;
        if (orderColumnStr == null) {
            orderColumn = ReportOrderColumn.REPORT_SEQ;
        } else {
            orderColumn = ReportOrderColumn.valueOf(orderColumnStr);
        }

        String filterColumnStr = param.get("filterColumn");
        ReportFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ReportFilterColumn.ADDR;
        } else {
            filterColumn = ReportFilterColumn.valueOf(filterColumnStr);
        }

        String search = "";
        String searchStr = param.get("searchStr");
        String searchStr2 = param.get("searchStr2");
        if (filterColumn.equals(ReportFilterColumn.RESULT)) {
            search = searchStr2;
        } else {
            if (searchStr == null) {
                searchStr = "";
            }
            search = searchStr.trim();
        }

        String orderDirectionStr = param.get("orderDirection");
        Sort.Direction direction;
        if (orderDirectionStr == null) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.valueOf(orderDirectionStr);
        }

        String pageSizeStr = param.get("pageSize");
        int pageSize = 10;
        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        ModelAndView modelAndView = new ModelAndView();


        Page<Report> pages = reportService.gets(pageNumber, pageSize, filterColumn, search, orderColumn, direction);
        ;

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        if (totalPages > 3 && (totalPages - pageNumber) > 2) {
            isEndOver = true;
        }

        if (totalPages > 3 && pageNumber > 1) {
            isBeginOver = true;
        }

        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("filterColumn", filterColumnStr);
        modelAndView.addObject("searchStr", searchStr);
        modelAndView.addObject("searchStr2", searchStr2);
        modelAndView.addObject("orderColumn", orderColumnStr);
        modelAndView.addObject("orderDirection", orderDirectionStr);

        modelAndView.addObject("pageNumber", pageNumber);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("isBeginOver", isBeginOver);
        modelAndView.addObject("isEndOver", isEndOver);
        modelAndView.addObject("reports", pages.getContent());

        modelAndView.addObject("mainTitle", mainTitle);
        modelAndView.addObject("subTitle", subTitle);
        modelAndView.setViewName(getPath("/reportList"));
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
